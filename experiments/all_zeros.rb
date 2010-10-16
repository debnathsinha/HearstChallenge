# prepare a submission of all zeros

if __FILE__ == $0
  
  output_lines = []
  # build output
  File.open("../data/template_for_submission.csv").each_with_index do |line, i|
    line = line.strip
    if i==0
      output_lines << line
      next #skip
    end
    store, title, yearmonth = line.split(",")
    output_lines << "#{store},#{title},#{yearmonth},#{0.0}"
    puts "#{i}..." if (i%1000)==0
  end
  
  # write file
  f = File.open("all_zeros_submission.csv", 'w')
  output_lines.each do |line|
    f.write(line + "\n")    
  end
  f.close
end