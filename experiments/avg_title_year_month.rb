# use the average sales for the title/year/month if available, otherwise 
# use the averages sales for the year/month

require "lib/submission"
require "lib/title_year_month_sales"
require "lib/year_month_sales"

class AvgTitleYearMonthZeros < Submission
  
  def initialize(filename)
    super(filename)
    # prepare database
    @yearMonthSales = YearMonthSales.new
    @yearMonthSales.load
    @titleYearMonthSales = TitleYearMonthSales.new
    @titleYearMonthSales.load
    # prepare counters
    @total_title_year_month = 0
    @total_year_month = 0
  end
  
  def output_filename
    return "avg_title_year_month_submission.csv"
  end
  
  def get_sales(store, title, year, month)
    # try title-year-month
    sales = @titleYearMonthSales.get_sales(title, year, month)
    # try year-month as a fallback
    if sales.nil?
      sales = @yearMonthSales.get_sales(year, month) 
      @total_year_month += 1
    else 
      @total_title_year_month += 1
    end
    return sales
  end
  
  def final_report
    puts "Total title/year/month: #{@total_title_year_month}"
    puts "Total year/month: #{@total_year_month}"
  end
  
end

if __FILE__ == $0
  # connect to the database
  ActiveRecord::Base.establish_connection(:adapter=>"mysql", :host => "localhost",
    :username=>"root", :database=>"hearst_challenge")
  # create submission 
  o = AvgTitleYearMonthZeros.new("../data/template_for_submission.csv")
  o.generate_submission
  o.final_report
end
