# try avg sales for chain, title, year, month
# then try avg sales for wholesaler, title, year, month
# then try avg sales for title, year, month

require "avg_wholesaler_title_year_month"

require "lib/chain_title_year_month_sales"
require "lib/chain_store_vd"

class AvgChainTitleYearMonth < AvgWholesalerTitleYearMonth
  
  def initialize(filename)
    super(filename)
    init_chain_sales
  end
  
  def init_chain_sales
    # data
    @chainTitleYearMonthSales = ChainTitleYearMonthSales.new
    @chainTitleYearMonthSales.load    
    # helper
    @chainStore = ChainStore.new
    @chainStore.load
    # counter
    @totals["chain/title/year/month"] = 0
  end
  
  def output_filename
    return "submissions/avg_chain_title_year_month_submission.csv"
  end
  
  def get_sales(store, title, year, month)
    sales = get_chain_sales(store, title, year, month)
    sales = get_wholesaler_sales(store, title, year, month) if sales.nil?
    sales = get_title_sales(store, title, year, month) if sales.nil?
    return sales
  end
  
  def get_chain_sales(store, title, year, month)
    chain = @chainStore.get_chain(store)
    raise "Could not find chain for store=#{store}" if chain.nil?
    sales = @chainTitleYearMonthSales.get_sales(chain, title, year, month)
    @totals["chain/title/year/month"] += 1 if !sales.nil?
    return sales
  end
  
end

if __FILE__ == $0
  # connect to the database
  ActiveRecord::Base.establish_connection(:adapter=>"mysql", :host => "localhost",
    :username=>"root", :database=>"hearst_challenge")
  # create submission 
  o = AvgChainTitleYearMonth.new("../data/template_for_submission.csv")
  o.generate_submission
  o.final_report
end
