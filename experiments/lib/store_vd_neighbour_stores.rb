
require "rubygems"
require "active_record"

class StoreNeighbourStoresAR < ActiveRecord::Base
  set_table_name "template_vd_store_neighbours_in_mo"
end

class StoreNeighbourStores
  
  def initialize
    @map = {}
  end
  
  def execute_query
    return StoreNeighbourStoresAR.all
  end
  
  def load
    rs = execute_query
    raise "Query failed" if rs.nil? or rs.size<=0
    rs.each do |row|
      key = to_key(row["store_key"], row["title_key"], row["on_year"], row["on_month"])
      value = row["neighbour_store_key"].to_i
      @map[key] = value
    end
    puts "ChainTitleYearMonthSales loaded #{@map.size} average sales results"
  end

  def get_neighbours(store, title, year, month)
    key = to_key(store, title, year, month)
    return @map[key]
  end
  
  def to_key(store, title, year, month)
    raise "Invalid key data: store=#{store}, title=#{title}, year=#{year}, month=#{month}" if store.nil? or title.nil? or year.nil? or month.nil?
    return "#{store}-#{title}-#{year}-#{month}"
  end
end

if __FILE__ == $0
  ActiveRecord::Base.establish_connection(:adapter=>"mysql", :host => "localhost",
    :username=>"root", :database=>"hearst_challenge")
  
  puts "Test for StoreNeighbourStores..."
  o = StoreNeighbourStores.new
  o.load  
  puts "done."
end