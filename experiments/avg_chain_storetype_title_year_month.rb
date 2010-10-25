# try avg sales for chain, title, year, month
# then try avg sales for storetype, title, year, month
# then try avg sales for wholesaler, title, year, month
# then try avg sales for title, year, month

require "avg_chain_title_year_month"

require "lib/city_chain_sales"
require "lib/store_storetype_vd"
require "lib/storetype_title_year_month_sales"

class AvgChainStoretypeTitleYearMonth < AvgChainTitleYearMonth
  
  def initialize(filename)
    super(filename)
    init_city_chain_sales
  end
  
  def init_city_chain_sales
    # data
    @storetypeTitleYearMonthSales = StoretypeTitleYearMonthSales.new
    @storetypeTitleYearMonthSales.load    
    # helper    
    @storeStoreType = StoreStoreType.new
    @storeStoreType.load
    # counter
    @totals["storetype/title/year/month"] = 0
  end
  
  def output_filename
    return "submissions/avg_chain_storetype_title_year_month_submission.csv"
  end
  
  def get_sales(store, title, year, month)
    sales = get_chain_sales(store, title, year, month)
    sales = get_storetype_sales(store, title, year, month) if sales.nil?
    sales = get_wholesaler_sales(store, title, year, month) if sales.nil?
    sales = get_title_sales(store, title, year, month) if sales.nil?
    return sales
  end
  
  def get_storetype_sales(store, title, year, month)
    storetype = @storeStoreType.get_storetype(store)
    raise "Could not find storetype for store=#{store}" if storetype.nil?
    
    sales = @storetypeTitleYearMonthSales.get_sales(storetype, title, year, month)
    @totals["storetype/title/year/month"] += 1 if !sales.nil?
    return sales
  end
end

if __FILE__ == $0
  # connect to the database
  ActiveRecord::Base.establish_connection(:adapter=>"mysql", :host => "localhost",
    :username=>"root", :database=>"hearst_challenge")
  # create submission 
  o = AvgChainStoretypeTitleYearMonth.new("../data/template_for_submission.csv")
  o.generate_submission
  o.final_report
end
