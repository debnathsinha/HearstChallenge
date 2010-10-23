
require "rubygems"
require "active_record"

class WholesalerStoreAR < ActiveRecord::Base
  set_table_name "wholesaler_store_vd"
end

class WholesalerStore
  
  def initialize
    @map = {}
  end
  
  def execute_query
    return WholesalerStoreAR.all
  end
  
  def load
    rs = execute_query
    raise "Query failed" if rs.nil? or rs.size<=0    
    rs.each do |row|
      key = to_key(row["store_key"], row["title_key"])
      value = row["wholesaler_key"].to_i
      @map[key] = value
    end
    puts "WholesalerStore loaded #{@map.size} wholesaler-store records"
  end

  def get_wholesaler(store, title)
    key = to_key(store, title)
    return @map[key]
  end
  
  def to_key(store, title)
    raise "Invalid key data: year=#{store}, month=#{title}" if store.nil? or title.nil?
    return "#{store}-#{title}"
  end
end

if __FILE__ == $0
  ActiveRecord::Base.establish_connection(:adapter=>"mysql", :host => "localhost",
    :username=>"root", :database=>"hearst_challenge")
  
  puts "Test for WholesalerStore..."
  o = WholesalerStore.new
  o.load  
  puts "done."
end