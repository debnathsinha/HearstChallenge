# try avg sales for wholesaler, title, year, month
# then try avg sales for title, year, month

require "avg_title_year_month"

require "lib/wholesaler_title_year_month_sales"
require "lib/wholesaler_store_vd"

class AvgWholesalerTitleYearMonth < AvgTitleYearMonth
  
  def initialize(filename)
    super(filename)
    # prep data
    init_wholesaler_sales
  end
  
  def init_wholesaler_sales
    # data
    @wholesalerTitleYearMonthSales = WholesalerTitleYearMonthSales.new
    @wholesalerTitleYearMonthSales.load
    # helper
    @wholesalerStore = WholesalerStore.new
    @wholesalerStore.load
    # counter
    @totals["wholesaler/title/year/month"] = 0
  end
  
  def output_filename
    return "submissions/avg_wholesaler_title_year_month_submission.csv"
  end
  
  def get_sales(store, title, year, month)
    sales = get_wholesaler_sales(store, title, year, month)
    sales = get_title_sales(store, title, year, month) if sales.nil?
    return sales
  end
  
  def get_wholesaler_sales(store, title, year, month)
    wholesaler = @wholesalerStore.get_wholesaler(store, title)
    raise "Could not find wholesaler for store=#{store}, title=#{title}" if wholesaler.nil?
    sales = @wholesalerTitleYearMonthSales.get_sales(wholesaler, title, year, month)
    @totals["wholesaler/title/year/month"] += 1 if !sales.nil?
    return sales
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
