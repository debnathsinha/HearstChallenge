
require "rubygems"
require "active_record"

class CityChainSalesAR < ActiveRecord::Base
  set_table_name "template_city_chain_vd"
end

class CityChainSales
  def initialize
    @map = {}
  end
  
  def execute_query
    return CityChainSalesAR.all
  end
  
  def load
    rs = execute_query
    raise "Query failed" if rs.nil? or rs.size<=0
    rs.each do |row|
      key = to_key(row["city"], row["chain_key"], row["title_key"], row["on_year"], row["on_month"])
      value = row["sales_total"].to_f
      @map[key] = value
    end
    puts "CityChainSales loaded #{@map.size} average sales results"
  end

  def get_sales(city, chain, title, year, month)
    key = to_key(city, chain, title, year, month)
    average = @map[key]
    return average
  end
  
  def to_key(city, chain, title, year, month)
    raise "Invalid key data: city=#{city}, chain=#{chain}, title=#{title}, year=#{year}, month=#{month}" if city.nil? or chain.nil? or title.nil? or year.nil? or month.nil?
    return "#{city}-#{chain}-#{title}-#{year}-#{month}"
  end
  
end

if __FILE__ == $0
  ActiveRecord::Base.establish_connection(:adapter=>"mysql", :host => "localhost",
    :username=>"root", :database=>"hearst_challenge")
  
  puts "Test for CityChainSales..."
  o = CityChainSales.new
  o.load  
  puts "done."
end