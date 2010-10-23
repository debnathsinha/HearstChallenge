
require "rubygems"
require "active_record"

class ChainTitleYearMonthSalesAR < ActiveRecord::Base
  set_table_name "chain_title_year_month_sales_td"
end

class ChainTitleYearMonthSales
  def initialize
    @map = {}
  end
  
  def execute_query
    return ChainTitleYearMonthSalesAR.all
  end
  
  def load
    rs = execute_query
    raise "Query failed" if rs.nil? or rs.size<=0
    rs.each do |row|
      key = to_key(row["chain_key"], row["title_key"], row["on_year"], row["on_month"])
      value = row["sales_total"].to_f
      @map[key] = value
    end
    puts "ChainTitleYearMonthSales loaded #{@map.size} average sales results"
  end

  def get_sales(chain, title, year, month)
    key = to_key(chain, title, year, month)
    average = @map[key]
    return average
  end
  
  def to_key(chain, title, year, month)
    raise "Invalid key data: chain=#{chain}, title=#{title}, year=#{year}, month=#{month}" if chain.nil? or title.nil? or year.nil? or month.nil?
    return "#{chain}-#{title}-#{year}-#{month}"
  end
  
end

if __FILE__ == $0
  ActiveRecord::Base.establish_connection(:adapter=>"mysql", :host => "localhost",
    :username=>"root", :database=>"hearst_challenge")
  
  puts "Test for ChainTitleYearMonthSales..."
  o = ChainTitleYearMonthSales.new
  o.load  
  puts "done."
end