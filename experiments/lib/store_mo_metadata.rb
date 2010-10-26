
require "rubygems"
require "active_record"

class StoreMoNeighbourMetadataAR < ActiveRecord::Base
  set_table_name "store_mo"
end

class StoreMoNeighbourMetadata
  
  def initialize(fields)    
    @fields = fields
  end
  
  def execute_query(stores)
    return StoreMoNeighbourMetadataAR.find_by_sql("select store_key,#{@fields.join(',')} from store_mo where store_key in (#{stores.join(',')})")
  end
  
  def get_store_metadata(stores)
    rs = execute_query(stores)
    raise "Query failed" if rs.nil? or rs.size<=0
    return rs
  end
  
  def get_k_neighbours(k, prototype, stores)
    # get meta data for stores
    metadata = get_store_metadata(stores)
    # calculate distances for each store metadata
    results = []
    metadata.each do |record| 
      results << {"store_key"=>record["store_key"], "distance"=>distance(prototype, record)}
    end
    # order the results
    results.sort!{|x,y| x["distance"]<=>y["distance"]}    
    # return the top k
    best_neighbour_stores = []
    results[0...k].each {|rec| best_neighbour_stores << rec["store_key"]}
    return best_neighbour_stores
  end
  
  # todo handle boolean!
  # todo handle missing values
  def distance(prototype, other)
    dist = 0.0
    # sum squared differences
    prototype.keys.each do |key|
      raise "Could not find #{key} in metadata: #{metadata.inspect}" if other[key].nil?
      diff = (prototype[key].to_f - other[key].to_f)
      raise "Bad difference #{diff} a=#{prototype[key]}, b=#{other[key]}" if diff==0.0/0.0
      dist += diff*diff
    end
    return Math.sqrt(dist)
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
  
  metadata = {}
  fields.each {|x| metadata[x] = 1.0 }
  puts metadata.inspect
  
  puts "Test for StoreMoNeighbourMetadata..."
  o = StoreMoNeighbourMetadata.new(fields)  
  neighbours = o.get_k_neighbours(3, metadata, [2,3,4,5])
  puts "Got #{neighbours.size} neighbours"
  neighbours.each do |n|
    puts " > #{n}"
  end
  puts "done."
end