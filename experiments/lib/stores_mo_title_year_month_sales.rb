
require "rubygems"
require "active_record"

class StoresMoTitleYearMonthSalesAR < ActiveRecord::Base
  set_table_name "template_mo"
end

class StoresMoTitleYearMonthSales

  def execute_query(stores, title, year, month)
    rs = StoresMoTitleYearMonthSalesAR.average("sales_total", :conditions=>"title_key=#{title} and on_year=#{year} and on_month=#{month} and store_key in (#{stores.join(',')})", :group=>'title_key, on_year, on_month')
    
    # WTF? - works though
    return rs.first[1]
  end

  def get_sales(stores, title, year, month)
    return execute_query(stores, title, year, month)
  end
end

if __FILE__ == $0
  ActiveRecord::Base.establish_connection(:adapter=>"mysql", :host => "localhost",
    :username=>"root", :database=>"hearst_challenge")
  
  puts "Test for StoreNeighbourStores..."
  o = StoresMoTitleYearMonthSales.new
  puts o.get_sales([2,3,4,5], 1, 2007, 8)  
  
  puts "done."
end