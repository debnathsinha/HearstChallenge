# try avg sales for city, chain, title, year, month
# then try avg sales for chain, title, year, month
# then try avg sales for wholesaler, title, year, month
# then try avg sales for title, year, month

require "avg_chain_title_year_month"

require "lib/city_chain_sales"
require "lib/chain_store_vd"
require "lib/store_city_vd"

class AvgCityChainTitleYearMonth < AvgChainTitleYearMonth
  
  def initialize(filename)
    super(filename)
    init_city_chain_sales
  end
  
  def init_city_chain_sales
    # data
    @cityChainSales = CityChainSales.new
    @cityChainSales.load    
    # helper
    @chainStore = ChainStore.new
    @chainStore.load
    @storeCity = StoreCity.new
    @storeCity.load
    # counter
    @totals["city/chain/title/year/month"] = 0
  end
  
  def output_filename
    return "submissions/avg_city_chain_title_year_month_submission.csv"
  end
  
  def get_sales(store, title, year, month)
    sales = get_city_chain_sales(store, title, year, month)
    sales = get_chain_sales(store, title, year, month) if sales.nil?
    sales = get_wholesaler_sales(store, title, year, month) if sales.nil?
    sales = get_title_sales(store, title, year, month) if sales.nil?
    return sales
  end
  
  def get_city_chain_sales(store, title, year, month)
    city = @storeCity.get_city(store)
    raise "Could not find city for store=#{store}" if city.nil?
    chain = @chainStore.get_chain(store)
    raise "Could not find chain for store=#{store}" if chain.nil?
    
    sales = @cityChainSales.get_sales(city, chain, title, year, month)
    @totals["city/chain/title/year/month"] += 1 if !sales.nil?
    return sales
  end
  
end

if __FILE__ == $0
  # connect to the database
  ActiveRecord::Base.establish_connection(:adapter=>"mysql", :host => "localhost",
    :username=>"root", :database=>"hearst_challenge")
  # create submission 
  o = AvgCityChainTitleYearMonth.new("../data/template_for_submission.csv")
  o.generate_submission
  o.final_report
end
