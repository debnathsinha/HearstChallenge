
require "rubygems"
require "active_record"

class StoreStoreTypeAR < ActiveRecord::Base
  set_table_name "store_storetype_vd"
end

class StoreStoreType
  
  def initialize
    @map = {}
  end
  
  def execute_query
    return StoreStoreTypeAR.all
  end
  
  def load
    rs = execute_query
    raise "Query failed" if rs.nil? or rs.size<=0    
    rs.each do |row|
      key = to_key(row["store_key"])
      value = row["store_type"]
      @map[key] = value
    end
    puts "StoreStoreType loaded #{@map.size} store-storetype records"
  end

  def get_storetype(store)
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
  
  puts "Test for StoreStoreType..."
  o = StoreStoreType.new
  o.load  
  puts "done."
end