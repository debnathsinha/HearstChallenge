
require "rubygems"
require "active_record"

class StoreVdMetadataAR < ActiveRecord::Base
  set_table_name "store_vd"
end

class StoreVdMetadata
  
  def initialize(fields)    
    @fields = fields
    @map = {}
  end
  
  def execute_query(stores)
    rs = StoreVdMetadataAR.find_by_sql("select store_key,#{@fields.join(',')} from store_vd where store_key in (#{stores.join(',')})")
    raise "Query failed" if rs.nil? or rs.size<=0   
    return rs
  end
  
  def get_stores
    rs = StoreVdMetadataAR.find_by_sql("select distinct store_key from template_vd")
    raise "Failed to get distinct stores" if rs.nil? or rs.size<=0    
    stores = []
    rs.each {|row| stores << row["store_key"]}
    return stores
  end
  
  def load
    stores = get_stores
    rs = execute_query(stores)
    rs.each do |row|
      key = to_key(row["store_key"])
      @map[key] = row
    end
    puts "StoreVdMetadata loaded #{@map.size} store metaata records records"
  end

  def get_metadata(store)
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
  
  fields = ["summarized_area_lvl_statistics_a", "summarized_area_lvl_statistics_b", "summarized_area_lvl_statistics_c", "summarized_area_lvl_statistics_d", "summarized_area_lvl_statistics_e", "summarized_area_lvl_statistics_f", "summarized_area_lvl_statistics_g", "summarized_area_lvl_statistics_h"]
  
  puts "Test for StoreVdMetadata..."
  o = StoreVdMetadata.new(fields)
  o.load  
  
  puts o.get_metadata(33).inspect
  
  puts "done."
end