# prepare a submission of all zeros

require "lib/submission"

class AllZeros < Submission
  def output_filename
    return "all_zeros_submission.csv"
  end
  
  def get_sales(store, title, year, month)
    return 0.0
  end
end

if __FILE__ == $0  
  o = AllZeros.new("../data/template_for_submission.csv")
  o.generate_submission
end