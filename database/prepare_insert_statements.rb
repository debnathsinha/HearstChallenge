# create insert files from the csv files

require 'csv'

def is_a_number?(s)
  s.to_s.match(/\A[+-]?\d+?(\.\d+)?\Z/) == nil ? false : true 
end

def create_file(filename, tablename)
  data = "connect hearst_challenge;\n"
  skipped_first = false
  expected_parts = 0
  CSV.open(filename, 'r') do |row|
    if !skipped_first
      skipped_first = true
      expected_parts = row.length
      next
    end

    # validate
    if row.length != expected_parts
      raise "got #{row.length} fields and expected #{expected_parts}, perhaps clean this row manually: #{row.join(',')}" 
    end
    
    # build up line data
    line_data = []
    row.each do |part|
      if part.nil? or part.empty?
        line_data << "null"
      elsif
        if is_a_number?(part)
          line_data << part
        else
          line_data << "\'#{part}\'"
        end
      end
    end
    
    data << "insert into #{tablename} values(#{line_data.join(',')});\n"  
  end
  output_filename = "#{tablename}_inserts.sql"
  f = File.open(output_filename, 'w')
  f.write(data)
  f.close
  puts "Read #{filename} and wrote #{output_filename}"
end


# validation
# create_file("../data/sales_vd_dataset.csv", "sales_vd")
# create_file("../data/store_vd_dataset.csv", "store_vd")
# create_file("../data/template_for_submission.csv", "template_vd")

# model

# insert dates as 'YYYY-MM-DD' not 'MM/DD/YYYY'
# create_file("../data/issue_mo_dataset.csv", "issue_mo")

# works
# create_file("../data/sales_mo_dataset.csv", "sales_mo")
# create_file("../data/store_mo_dataset.csv", "store_mo")
create_file("../data/wholesaler_mo_dataset.csv", "wholesaler_mo")
