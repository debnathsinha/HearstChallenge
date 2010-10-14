# 



def create_file(filename, tablename)
  data = "connect hearst_challenge;\n"
  skipped_first = false
  File.open("../data/#{filename}").each do |line|
    if !skipped_first
      skipped_first = true
      next
    end
    data << "insert into #{tablename} values(#{line.strip});\n"  
  end
  output_filename = "#{tablename}_inserts.sql"
  f = File.open(output_filename, 'w')
  f.write(data)
  f.close
  puts "Read #{filename} and wrote #{output_filename}"
end


# validation

# works
# create_file("Hearst_Challenge_Validation_CSV_Files/sales_vd_dataset.csv", "sales_vd")

# TODO deal with strings
# create_file("Hearst_Challenge_Validation_CSV_Files/store_vd_dataset.csv", "store_vd")

# TODO deal with missing value
# create_file("Hearst_Challenge_Validation_CSV_Files/template_for_submission.csv", "template_vd")

# model

#  TODO dates and missing values
# create_file("Hearst_Challenge_Modeling_CSV_Files/issue_mo_dataset.csv", "issue_mo")

# works
# create_file("Hearst_Challenge_Modeling_CSV_Files/sales_mo_dataset.csv", "sales_mo")

# TODO deal with strings
# create_file("Hearst_Challenge_Modeling_CSV_Files/store_mo_dataset.csv", "store_mo")

# TODO deal with string
# create_file("Hearst_Challenge_Modeling_CSV_Files/wholesaler_mo_dataset.csv", "wholesaler_mo")
