

require "lib/submission"

require "lib/store_vd_neighbour_stores2"
require "lib/store_mo_metadata"
require "lib/store_vd_metadata"
require "lib/stores_mo_title_year_month_sales"

class AvgSimpleNeighbourTitleYearMonth < Submission
  
  def initialize(filename)
    super(filename)
    init_neighbour_sales
  end
  
  def init_neighbour_sales
    fields = ["summarized_area_lvl_statistics_a", "summarized_area_lvl_statistics_b", "summarized_area_lvl_statistics_c", "summarized_area_lvl_statistics_d", "summarized_area_lvl_statistics_e", "summarized_area_lvl_statistics_f", "summarized_area_lvl_statistics_g", "summarized_area_lvl_statistics_h"]
    
    @kParameter = 20
    
    # helpers
    @storeVdMetadata = StoreVdMetadata.new(fields)
    @storeVdMetadata.load
    
    @storeAvgSalesMo = StoresMoTitleYearMonthSales.new
    
    @storeNeighbours = StoreNeighbourStores.new
    
    @storeMoMetadata = StoreMoNeighbourMetadata.new(fields)
    
    @totals["neighbours/title/year/month"] = 0
  end
  
  def output_filename
    return "submissions/avg_simple_neighbour_title_year_month_submission.csv"
  end
  
  def get_sales(store, title, year, month)
    sales = get_neighbour_sales(store, title, year, month)
    raise "Failed to calculate sales for store=#{store}, title=#{title}, year=#{year}, month=#{month}" if sales.nil?
    
    puts " >Finished store=#{store}, title=#{title}, year=#{year}, month=#{month}"
    
    return sales
  end
  
  def get_neighbour_sales(store, title, year, month)
    # get store metadata
    store_metadata = @storeVdMetadata.get_metadata(store)
    # get candidate neighbouring stores
    stores = @storeNeighbours.get_neighbours(store, title, year, month)
    # get n best neighbouring stores
    neighbour_stores = @storeMoMetadata.get_k_neighbours(@kParameter, store_metadata, stores)
    # get the average sales
    sales = @storeAvgSalesMo.get_sales(neighbour_stores, title, year, month)
    # update totals
    @totals["neighbours/title/year/month"] += 1 if !sales.nil?
    return sales
  end
end

if __FILE__ == $0
  # connect to the database
  ActiveRecord::Base.establish_connection(:adapter=>"mysql", :host => "localhost",
    :username=>"root", :database=>"hearst_challenge")
  # create submission 
  o = AvgSimpleNeighbourTitleYearMonth.new("../data/template_for_submission.csv")
  o.generate_submission
  o.final_report
end
