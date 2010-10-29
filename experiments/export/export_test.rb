
require "rubygems"
require "active_record"

class DB < ActiveRecord::Base
  set_table_name "template_vd3"
end

class ExportTest
  
  def initialize
    @fields = ["store_key","title_key","on_year","on_month"]
  end
  
  def execute_query
    rs =  DB.find_by_sql("select #{@fields.join(',')} from template_vd3")
    raise "Query failed" if rs.nil? or rs.size<=0    
    return rs
  end
  
  def load
    puts "Perforing query..."
    rs = execute_query
    puts "Got #{rs.size} records"
    lines = Array.new(rs.size)
    rs.each_with_index do |row, i|
      values = []
      @fields.each {|f| values << row[f]}
      lines[i] = values.join(',')
      puts "processed #{i} lines..." if (i%20000)==0 
    end
    write_output_file(lines, "export_test.dat")
  end


  def write_output_file(lines_array, filename)
    f = File.open(filename, 'w')
    lines_array.each {|line| f.write(line + "\n")} 
    f.close
    puts "Wrote #{lines_array.length} lines to file '#{filename}'"
  end
  
end

if __FILE__ == $0
  ActiveRecord::Base.establish_connection(:adapter=>"mysql", :host => "localhost",
    :username=>"root", :database=>"hearst_challenge")
  
  puts "Exporting test data."
  o = ExportTest.new
  o.load  
  puts "done."
end