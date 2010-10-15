# create insert files from the csv files

# updated to write a line as its processed - much slower but
# can deal with the really large files we have

require 'rubygems'
require 'fastercsv'

DATABASE_NAME = "hearst_challenge"
BUFFER_SIZE = 50000

def is_a_number?(s)
  s.to_s.match(/\A[+-]?\d+?(\.\d+)?\Z/) == nil ? false : true 
end

def csv_field_to_database_field(part)
  # empty
  if part.nil? or part.empty?
    return "null"
  end
  # number
  if is_a_number?(part)
    return part
  end
  # date
  if part.include?("/")
    # we want dates as 'YYYY-MM-DD' not 'MM/DD/YYYY'
    date_parts = part.split("/")
    return "\'#{date_parts[2]}-#{date_parts[0]}-#{date_parts[1]}\'"
  end
  # string
  return "\'#{part}\'"
end

def write(file, buffer, data, force=false)
  buffer << data if !data.nil?
  if force or buffer.length >= BUFFER_SIZE
    string = buffer.join("\n")
    file.write(string)
    file.write("\n")
    buffer.clear
  end  
end

def create_file(filename, tablename, validate=true)
  output_filename = "#{tablename}_inserts.sql"
  f = File.open(output_filename, 'w')
  buffer = []
  write(f, buffer, "connect #{DATABASE_NAME};")
  skipped_first = false
  expected_parts = 0
  FasterCSV.foreach(filename) do |row|
    # skip header line
    if !skipped_first
      skipped_first = true
      expected_parts = row.length
      next
    end
    # validate
    if validate and row.length != expected_parts
      raise "got #{row.length} fields and expected #{expected_parts}, perhaps clean this row manually: #{row.join(',')}" 
    end    
    # build up line data
    line_data = []
    row.each do |part|
      line_data << csv_field_to_database_field(part)
    end    
    # special handing for submission file    
    if !validate and row.length != expected_parts
      line_data << "null"
    end
    # add data
    write(f, buffer, "insert into #{tablename} values(#{line_data.join(',')});")
  end  
  write(f, buffer, nil, true)
  f.close
  # done  
  puts "Read #{filename} and wrote #{output_filename}"
end

puts "starting..."

# validation
create_file("../data/sales_vd_dataset.csv", "sales_vd")
create_file("../data/store_vd_dataset.csv", "store_vd")
create_file("../data/template_for_submission.csv", "template_vd", false)

# model
create_file("../data/issue_mo_dataset.csv", "issue_mo")
create_file("../data/sales_mo_dataset.csv", "sales_mo")
create_file("../data/store_mo_dataset.csv", "store_mo")
create_file("../data/wholesaler_mo_dataset.csv", "wholesaler_mo")

# big files...
create_file("../data/zip_plus4_data_1.csv", "zip_plus4_data1")
create_file("../data/zip_plus4_data_2.csv", "zip_plus4_data2")
create_file("../data/zip_plus4_data_3.csv", "zip_plus4_data3")
create_file("../data/zip_plus4_data_4.csv", "zip_plus4_data4")
create_file("../data/zip_plus4_data_5.csv", "zip_plus4_data5")

puts "done."
