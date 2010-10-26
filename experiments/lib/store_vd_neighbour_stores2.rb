
require "rubygems"
require "active_record"

class StoreNeighbourStoresAR < ActiveRecord::Base
  set_table_name "template_mo"
end

class StoreNeighbourStores
  

  
  def execute_query(store, title, year, month)
    rs = StoreNeighbourStoresAR.find_by_sql(["select distinct mo.store_key from template_vd3 vd inner join template_mo mo using (title_key, on_month, on_year) where vd.store_key=? AND vd.title_key=? AND vd.on_year=? AND vd.on_month=?", store, title, year, month])
    raise "Query failed" if rs.nil? or rs.size<=0
    return rs
  end


  def get_neighbours(store, title, year, month)
    rs = execute_query(store, title, year, month)
    neighbours = []
    rs.each {|row| neighbours << row["store_key"] }
    return neighbours
  end
  
  
end

if __FILE__ == $0
  ActiveRecord::Base.establish_connection(:adapter=>"mysql", :host => "localhost",
    :username=>"root", :database=>"hearst_challenge")
  
  puts "Test for StoreNeighbourStores..."
  o = StoreNeighbourStores.new
  n = o.get_neighbours(33, 1, 2007, 8)
  puts " got #{n.size} neighbours: #{n.inspect}"
  
  puts "done."
end