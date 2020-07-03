-- drop table if exists Flight_Info; 
-- Create temporary table if not exists Flight_Info
-- select 
-- fo.airline_id, fo.flight_number, 
-- fo.depart_time, fo.arrival_time, 
-- fo.departure_airport, fo.destination_airport,
-- t.booking_fee + t.ticket_price as 'total price', hs.seat_number, hs.seat_class
-- from Flight_Operates fo join (Tickets t, Has_Seats hs)
-- on fo.aircraft_id = t.aircraft_id and hs.aircraft_id = t.aircraft_id; 

-- select * from Flight_Info; 

-- CREATE temporary table IF NOT EXISTS Displayed_Flights
-- select 
-- fo.airline_id, fo.flight_number, 
-- fo.depart_time, fo.arrival_time, 
-- fo.departure_airport, fo.destination_airport,
-- t.booking_fee + t.ticket_price as 'total price'
-- from Flight_Operates fo join Tickets t
-- on fo.flight_number = t.flight_number; 

-- select * from Displayed_Flights; 

-- -- -- df.airline_id, df.flight_number, 
-- -- -- df.depart_time, df.arrival_time, 
-- -- -- df.departure_airport, df.destination_airport,
-- -- -- t.booking_fee + t.ticket_price as 'total price'

-- -- select 
-- -- t.ticket_number
-- -- from Displayed_Flights df join Tickets t 
-- -- on df.flight_number = t.flight_number and df.flight_number = '34'; 

-- select * from Person; 

-- Query to check availibility on a certain flight -- 
	-- WHERE = var in java based on user input -- 
SELECT  
case hs.seat_class
	when 'e' then (hs.total_economy_seats - economy_filled) as 'Economy Seats Availible'
    when 'b' then (hs.total_business_seats - business_filled) as 'Business Class Seats Availible'
    when 'f' then (hs.total_firstclass_seats - firstclass_filled) as 'First Class Seats Availible'
from Has_Seats hs join Flight_Operates fo
on hs.aircraft_id = fo.aircraft_id
where seat_class = 'c'; 


















