# try avg sales for wholesaler, title, year, month
# then try avg sales for title, year, month

require "lib/submission"
require "lib/title_year_month_sales"
require "lib/wholesaler_title_year_month_sales"
require "lib/wholesaler_store_vd"

class AvgWholesalerTitleYearMonth < Submission
  
  def initialize(filename)
    super(filename)
    # prepare database
    @wholesalerTitleYearMonthSales = WholesalerTitleYearMonthSales.new
    @wholesalerTitleYearMonthSales.load
    @titleYearMonthSales = TitleYearMonthSales.new
    @titleYearMonthSales.load
    @storeTitleToWholesaler = WholesalerStore.new
    @storeTitleToWholesaler.load
    # prepare counters
    @total_wholesaler_title_year_month = 0
    @total_title_year_month = 0
  end
  
  def output_filename
    return "avg_wholesaler_title_year_month_submission.csv"
  end
  
  def get_sales(store, title, year, month)
    # get wholesaler id
    wholesaler = @storeTitleToWholesaler.get_wholesaler(store, title)
    raise "Could not find wholesaler for store=#{store}, title=#{title}" if wholesaler.nil?
    # try wholesaler-title-year-month
    sales = @wholesalerTitleYearMonthSales.get_sales(wholesaler.to_i, title, year, month)
    # try title-year-month as a fallback
    if sales.nil?
      sales = @titleYearMonthSales.get_sales(title, year, month) 
      raise "no data for: title=#{title}, year=#{year}, month=#{month}" if sales.nil?
      @total_title_year_month += 1
    else 
      @total_wholesaler_title_year_month += 1
    end
    return sales
  end
  
  def final_report
    puts "Total wholesaler/title/year/month: #{@total_wholesaler_title_year_month}"
    puts "Total title/year/month: #{@total_title_year_month}"
  end
  
end

if __FILE__ == $0
  # connect to the database
  ActiveRecord::Base.establish_connection(:adapter=>"mysql", :host => "localhost",
    :username=>"root", :database=>"hearst_challenge")
  # create submission 
  o = AvgWholesalerTitleYearMonth.new("../data/template_for_submission.csv")
  o.generate_submission
  o.final_report
end
