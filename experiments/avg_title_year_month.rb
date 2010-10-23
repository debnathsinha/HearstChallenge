# use the average sales for the title/year/month if available, otherwise 
# use the averages sales for the year/month

require "lib/submission"
require "lib/title_year_month_sales"
require "lib/year_month_sales"

class AvgTitleYearMonth < Submission
  
  def initialize(filename)
    super(filename)
    @totals = {}
    # prep data
    init_title_sales
  end
  
  def init_title_sales
    # data
    @titleYearMonthSales = TitleYearMonthSales.new
    @titleYearMonthSales.load
    # counter
    @totals["title/year/month"] = 0
  end
  
  def output_filename
    return "avg_title_year_month_submission.csv"
  end
  
  def get_sales(store, title, year, month)
    return get_title_sales(store, title, year, month)
  end
  
  def get_title_sales(store, title, year, month)
    sales = @titleYearMonthSales.get_sales(title, year, month) 
    raise "no data for: title=#{title}, year=#{year}, month=#{month}" if sales.nil?
    @totals["title/year/month"] += 1 if !sales.nil?
    return sales
  end
  
  def final_report
    puts ""
    @totals.keys.sort.each {|key| puts "Total #{key} records: #{@totals[key]}" }
  end
  
end

if __FILE__ == $0
  # connect to the database
  ActiveRecord::Base.establish_connection(:adapter=>"mysql", :host => "localhost",
    :username=>"root", :database=>"hearst_challenge")
  # create submission 
  o = AvgTitleYearMonth.new("../data/template_for_submission.csv")
  o.generate_submission
  o.final_report
end
