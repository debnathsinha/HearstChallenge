
require "rubygems"
require "active_record"

class ChainStoreAR < ActiveRecord::Base
  set_table_name "store_chain_vd"
end

class ChainStore
  
  def initialize
    @map = {}
  end
  
  def execute_query
    return ChainStoreAR.all
  end
  
  def load
    rs = execute_query
    raise "Query failed" if rs.nil? or rs.size<=0    
    rs.each do |row|
      key = to_key(row["store_key"])
      value = row["chain_key"].to_i
      @map[key] = value
    end
    puts "ChainStore loaded #{@map.size} chain-store records"
  end

  def get_chain(store)
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
  
  puts "Test for ChainStore..."
  o = ChainStore.new
  o.load  
  puts "done."
end