# create insert files from the csv files

require 'csv'

DATABASE_NAME = "hearst_challenge"

def is_a_number?(s)
  s.to_s.match(/\A[+-]?\d+?(\.\d+)?\Z/) == nil ? false : true 
end

def write_string_to_file(string, filename)
  f = File.open(filename, 'w')
  f.write(string)
  f.close
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

def create_file(filename, tablename, validate=true)
  data = "connect #{DATABASE_NAME};\n"
  skipped_first = false
  expected_parts = 0
  CSV.open(filename, 'r') do |row|
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
      line_data << csv_field_to_database_field(part.strip)
    end    
    # special handing for submission file    
    if !validate and row.length != expected_parts
      line_data << ",null"
    end
    # add data
    data << "insert into #{tablename} values(#{line_data.join(',')});\n"  
  end  
  # write file
  output_filename = "#{tablename}_inserts.sql"
  write_string_to_file(data, output_filename)
  # done
  puts "Read #{filename} and wrote #{output_filename}"
end


# validation
# create_file("../data/sales_vd_dataset.csv", "sales_vd")
# create_file("../data/store_vd_dataset.csv", "store_vd")
create_file("../data/template_for_submission.csv", "template_vd", false)

# model
# create_file("../data/issue_mo_dataset.csv", "issue_mo")
# create_file("../data/sales_mo_dataset.csv", "sales_mo")
# create_file("../data/store_mo_dataset.csv", "store_mo")
# create_file("../data/wholesaler_mo_dataset.csv", "wholesaler_mo")

# big files...