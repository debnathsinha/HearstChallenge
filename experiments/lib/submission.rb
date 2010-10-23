
# SUBMISSION_TEMPLATE_FILENAME = "../../data/template_for_submission.csv"

class Submission
  def initialize(filename)
    @template_filename = filename
  end

  def output_filename
    raise "output_filename is undefined"
  end
  
  def get_sales(store, title, year, month)
    raise "get_sales(store, title, year, month) is undefined"
  end
  
  def generate_submission
    output_lines = []
    # build output
    puts "Processing template file: '#{@template_filename}'"
    File.open(@template_filename).each_with_index do |line, i|
      line = line.strip
      if i==0
        output_lines << line
        next #skip
      end
      store, title, yearmonth = line.split(",")
      year, month = yearmonth[0,4], yearmonth[4,5]
      sales = get_sales(store.to_i, title.to_i, year.to_i, month.to_i)
      output_lines << "#{store},#{title},#{yearmonth},#{sales}"
      puts "processed #{i} lines..." if (i%20000)==0
    end
    # write file
    write_output_file(output_lines, output_filename)
  end
  
  def write_output_file(lines_array, filename)
    f = File.open(filename, 'w')
    lines_array.each {|line| f.write(line + "\n")} 
    f.close
    puts "Wrote #{lines_array.length} lines to file '#{filename}'"
  end
end


# demonstration
if __FILE__ == $0  
  o = Submission.new("../../data/template_for_submission.csv")
  o.generate_submission
end