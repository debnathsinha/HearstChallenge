
require "rubygems"
require "active_record"

class StoreCityAR < ActiveRecord::Base
  set_table_name "store_city_vd"
end

class StoreCity
  
  def initialize
    @map = {}
  end
  
  def execute_query
    return StoreCityAR.all
  end
  
  def load
    rs = execute_query
    raise "Query failed" if rs.nil? or rs.size<=0    
    rs.each do |row|
      key = to_key(row["store_key"])
      value = row["city"]
      @map[key] = value
    end
    puts "StoreCity loaded #{@map.size} chain-store records"
  end

  def get_city(store)
    key = to_key(store)
    return @map[key]
  end
  
  def to_key(store)
    raise "Invalid key data: store=#{store}" if store.nil?
    return "#{store}"
  end
end

if __FILE__ == $0
  ActiveRecord::Base.establish_connection(:adapter=>"mysql", :host => "localhost",
    :username=>"root", :database=>"hearst_challenge")
  
  puts "Test for StoreCity..."
  o = StoreCity.new
  o.load  
  puts "done."
end