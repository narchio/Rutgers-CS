
-- insert into Aircraft(aircraft_id) values (34); 
-- insert into Aircraft(aircraft_id) values (35); 

-- insert into Airline(airline_id) values ('DT'); 
-- insert into Airline(airline_id) values ('UN');

-- insert into Airport(airport_id) values ('EWR'); 
-- insert into Airport(airport_id) values ('LAX');
 
-- delete from Flight_Operates; 

-- select * from Flight_Operates; 

 

-- insert into Flight_Operates(aircraft_id, airline_id, 
-- airport_id, flight_number, 
-- flight_type, depart_time, arrival_time, 
-- fare_first, fare_economy, departure_airport,
-- destination_airport, fare_business, 
-- num_stops, depart_date, return_date)
-- Values (12, 'UN', 'LAX', 41, 'o', '02:13:00', '11:00:00', 
-- 12.20, 14.30, 'LAX', 'EWR', 12.00, 0, '1998-02-02', '1998-02-10'); 


-- insert into Flight_Operates(aircraft_id, airline_id, 
-- airport_id, flight_number, 
-- flight_type, depart_time, arrival_time, 
-- fare_first, fare_economy, departure_airport,
-- destination_airport, fare_business, 
-- num_stops, depart_date, return_date)
-- Values (34, 'DT', 'EWR', 11, 'roundtrip', '09:40:00', '11:00:00', 
-- 12.20, 14.30, 'EWR', 'LAX', 12.00, 0, '1998-07-24', '1998-07-28'); 


-- insert into Flight_Operates(aircraft_id, airline_id, 
-- airport_id, flight_number, 
-- flight_type, depart_time, arrival_time, 
-- fare_first, fare_economy, departure_airport,
-- destination_airport, fare_business, 
-- num_stops, depart_date, return_date)
-- Values (34, 'DT', 'LAX', 13, 'oneway', '01:10:00', '01:50:00', 
-- 12.20, 14.30, 'LAX', 'EWR', 12.00, 0, '1998-07-10', '1998-07-12'); 

-- insert into Tickets(ticket_number, seat_number, account_num, 
-- aircraft_id, airline_id, airport_id, flight_number,
-- seat_class, booking_fee, issue_date, ticket_price)
-- Values (1, 1, 1, 34, 'DT', 'EWR', 9, 'e', 5, '1998-06-01 01:00:00', 14.3);

-- insert into Tickets(ticket_number, seat_number, account_num, 
-- aircraft_id, airline_id, airport_id, flight_number,
-- seat_class, booking_fee, issue_date, ticket_price)
-- Values (2, 4, 2, 34, 'DT', 'EWR', 9, 'b', 5, '1998-06-01 11:00:00', 14.3);

-- insert into Tickets(ticket_number, seat_number, account_num, 
-- aircraft_id, airline_id, airport_id, flight_number,
-- seat_class, booking_fee, issue_date, ticket_price)
-- Values (4, 7, -1, 34, 'DT', 'EWR', 10, 'b', 5, '1998-06-01 13:00:00', 14.3);

-- delete from Tickets where account_num = 4; 

-- select * from Tickets; 


-- select distinct flight_number from Flight_Operates;

-- select * from Flight_Operates; 
-- select * from Tickets; 

-- update Tickets 
-- set account_num = 3
-- where  seat_class = 'b' and flight_number = 9 and issue_date = '1998-06-01 11:00:00'; 

-- update Tickets 
-- set account_num = -1
-- where  seat_class = 'b' and flight_number = 9; 

select * from Tickets; 

drop temporary table if exists temp; 

create temporary table if not exists temp
select distinct fo.airline_id, fo.flight_number, 
t.seat_class, fo.depart_time, fo.arrival_time, 
fo.departure_airport, fo.destination_airport, 
fo.num_stops, t.ticket_price as 'Price' 
from Flight_Operates fo join Tickets t 
on fo.aircraft_id = t.aircraft_id and fo.airline_id = t.airline_id
and fo.airport_id = t.airport_id and fo.flight_number = t.flight_number
where fo.flight_type = 'r' and t.seat_class = 'b' and departure_airport = 'EWR' and
destination_airport = 'LAX' and fo.depart_date <= '1998-07-22' and fo.return_date >= '1998-07-19'; 

select * from temp; 


drop temporary table if exists temp2; 

create temporary table if not exists temp2
select distinct fo.airline_id, fo.flight_number, 
t.seat_class, fo.depart_time, fo.arrival_time, 
fo.departure_airport, fo.destination_airport, 
fo.num_stops, t.ticket_price as 'Price', t.ticket_number 
from Flight_Operates fo join Tickets t 
on fo.aircraft_id = t.aircraft_id and fo.airline_id = t.airline_id
and fo.airport_id = t.airport_id and fo.flight_number = t.flight_number
where fo.flight_type = 'r' and t.seat_class = 'b' and departure_airport = 'EWR' and
destination_airport = 'LAX' and fo.depart_date <= '1998-07-22' and fo.return_date >= '1998-07-19'
and t.account_num = -1 and t.flight_number = 9 and fo.airline_id = 'DT'; 

select * from temp2; 


-- Update Tickets
-- set account_num = 33
-- where flight_number = 9 and airline_id = 'DT' and 
-- ticket_number in (select MIN(ticket_number) 
-- 				from (select distinct fo.airline_id, fo.flight_number, 
-- t.seat_class, fo.depart_time, fo.arrival_time, 
-- fo.departure_airport, fo.destination_airport, 
-- fo.num_stops, t.ticket_price as 'Price', t.ticket_number 
-- from Flight_Operates fo join Tickets t 
-- on fo.aircraft_id = t.aircraft_id and fo.airline_id = t.airline_id
-- and fo.airport_id = t.airport_id and fo.flight_number = t.flight_number
-- where fo.flight_type = 'r' and t.seat_class = 'b' and departure_airport = 'EWR' and
-- destination_airport = 'LAX' and fo.depart_date <= '1998-07-22' and fo.return_date >= '1998-07-19'
-- and t.account_num = -1 and t.flight_number = 9 and fo.airline_id = 'DT') as temp3); 

-- select MIN(ticket_number) as 'ticket_num'
-- from (select distinct fo.airline_id, fo.flight_number, 
-- t.seat_class, fo.depart_time, fo.arrival_time, 
-- fo.departure_airport, fo.destination_airport, 
-- fo.num_stops, t.ticket_price as 'Price', t.ticket_number 
-- from Flight_Operates fo join Tickets t 
-- on fo.aircraft_id = t.aircraft_id and fo.airline_id = t.airline_id
-- and fo.airport_id = t.airport_id and fo.flight_number = t.flight_number
-- where fo.flight_type = 'r' and t.seat_class = 'b' and departure_airport = 'EWR' and
-- destination_airport = 'LAX' and fo.depart_date <= '1998-07-22' and fo.return_date >= '1998-07-19'
-- and t.account_num = -1 and t.flight_number = 9 and fo.airline_id = 'DT') as temp3; 
--  

-- Update Tickets
-- set account_num = 3
-- where flight_number = 9 and airline_id = 'DT' and 
-- ticket_number in (select MIN(ticket_number) 
-- 				from temp2); 

-- Update Tickets
-- set account_num = 9
-- where flight_number = 9 and airline_id = 'DT' and 
-- ticket_number = 2; 

-- select * from Tickets; 


-- insert into Reserves(ticket_number, account_num)
-- values(3,3); 

-- select * from Reserves; 
-- select * from Person; 


-- update Person set logged_in = -1; 
-- select account_num from Person where logged_in = 599424; 

-- select * from Person; 
-- select account_num from Person; 

-- select * from Tickets; 


-- select distinct fo.airline_id, fo.flight_number, t.seat_class, fo.depart_time, fo.arrival_time, fo.departure_airport, fo.destination_airport, fo.num_stops, t.ticket_price as 'Price' from Flight_Operates fo join Tickets t on fo.aircraft_id = t.aircraft_id and fo.airline_id = t.airline_id and fo.airport_id = t.airport_id and fo.flight_number = t.flight_number where fo.flight_type = 'r' and t.seat_class = 'b' and fo.departure_airport = 'EWR' and fo.destination_airport = 'LAX' and fo.depart_date between '1998-07-15' and '1998-07-21' and fo.return_date between '1998-07-19' and '1998-07-25' and t.account_num = -1 and t.flight_number = 9 and fo.airline_id = 'DT';


-- UPDATE Tickets set account_num = 176008664 where flight_number = 9 and airline_id = DT and ticket_number = 2; 
-- select * from Reserves; 

-- select * from Flight_Operates; 


-- select fo.airline_id, fo.flight_number, t.seat_class, fo.depart_time, fo.arrival_time, fo.departure_airport, fo.destination_airport, fo.num_stops, t.ticket_price as 'Price', t.ticket_number, t.account_num
-- from Flight_Operates fo JOIN Tickets t 
-- on fo.aircraft_id = t.aircraft_id and fo.airline_id = t.airline_id
-- and fo.airport_id = t.airport_id and fo.flight_number = t.flight_number
-- where fo.depart_date between '1998-07-18' and '1998-07-22'
-- and fo.return_date between '1998-07-18' and '1998-07-22' and 
-- t.ticket_number in (select r.ticket_number from Reserves r where r.account_num = 176008664);  




update Tickets
set account_num = -1;  
delete from Reserves; 
delete from Waitlist; 

select * from Waitlist; 
select * from Reserves; 
select * from Tickets;

select * from Person; 

update Person
set logged_in = -1 ; 



-- select MIN(ticket_number) as 'ticket_num' 
-- from ( select distinct fo.airline_id, fo.flight_number, 
-- t.seat_class, fo.depart_time, fo.arrival_time, fo.departure_airport, 
-- fo.destination_airport, fo.num_stops, t.ticket_price as 'Price', t.ticket_number 
-- from Flight_Operates fo join Tickets t 
-- on fo.aircraft_id = t.aircraft_id and fo.airline_id = t.airline_id 
-- and fo.airport_id = t.airport_id and fo.flight_number = t.flight_number 
-- where fo.flight_type = 'r' and t.seat_class = 'b' and fo.departure_airport = 'EWR' 
-- and fo.destination_airport = 'LAX' and fo.depart_date between '1998-07-17' 
-- and '1998-07-23' and fo.return_date between '1998-07-17' and '1998-07-23' 
-- and t.account_num = -1 and t.flight_number = 10 and t.airline_id = 'DT') as temp3; 

 
delete from Waitlist; 
select * from Waitlist; 
-- INSERT INTO Waitlist(flight_number, airline_id, account_num) VALUES (10, 'DT', 176008665)

select t.account_num, t.ticket_number, fo.airline_id, fo.flight_number, t.seat_class, fo.depart_time, fo.arrival_time, fo.departure_airport, fo.destination_airport, fo.num_stops, t.ticket_price as 'Price'
			from Flight_Operates fo JOIN Tickets t 
			on fo.aircraft_id = t.aircraft_id and fo.airline_id = t.airline_id
			and fo.airport_id = t.airport_id and fo.flight_number = t.flight_number
			where t.ticket_number in (select r.ticket_number from Reserves r where r.account_num = 1);

select * from Person; 
-- update Person 
-- set logged_in = -1
-- where logged_in = ; 



select fo.airline_id, fo.flight_number, t.seat_class, fo.depart_time, fo.arrival_time, fo.departure_airport, fo.destination_airport, fo.num_stops, t.ticket_price as 'Price', t.ticket_number, t.account_num
			from Flight_Operates fo JOIN Tickets t 
			on fo.aircraft_id = t.aircraft_id and fo.airline_id = t.airline_id
			and fo.airport_id = t.airport_id and fo.flight_number = t.flight_number
			where t.ticket_number in (select r.ticket_number from Reserves r where r.account_num = 1)
            and fo.depart_date >= '1998-07-18' and fo.return_date >= '1998-07-18';
            
            select fo.airline_id, fo.flight_number, t.seat_class, fo.depart_time, fo.arrival_time, fo.departure_airport, fo.destination_airport, fo.num_stops, t.ticket_price as 'Price', t.ticket_number, t.account_num
			from Flight_Operates fo JOIN Tickets t 
			on fo.aircraft_id = t.aircraft_id and fo.airline_id = t.airline_id
			and fo.airport_id = t.airport_id and fo.flight_number = t.flight_number
			where fo.depart_date <= '1998-07-22' and fo.return_date <= '1998-07-22' and
			t.ticket_number in (select r.ticket_number from Reserves r where r.account_num = 1);


-- select seat_class, ticket_number, account_num from Tickets where ticket_number = 2 and account_num = 1 and seat_class = 'b' or seat_class = 

select seat_class from Tickets where ticket_number = 2 and account_num = -1; 



select t.account_num, t.ticket_number, fo.airline_id, fo.flight_number, t.seat_class, fo.depart_time, fo.arrival_time, fo.departure_airport, fo.destination_airport, fo.num_stops, t.ticket_price as 'Price'
			from Flight_Operates fo JOIN Tickets t 
			on fo.aircraft_id = t.aircraft_id and fo.airline_id = t.airline_id
			and fo.airport_id = t.airport_id and fo.flight_number = t.flight_number
			where t.ticket_number in (select r.ticket_number from Reserves r where r.account_num = 1) and t.ticket_number != 1;
            
            
            
            
-- update Person set account_type = 2 where username = 'cr1'; 
--             
-- insert into Person(username, password, name, account_num, logged_in, account_type)
-- values ('cr1', 'cr1', 'custrep1', 19, -1, 2); 
select * from Tickets; 

select * from Reserves;

select * from Person; 

update Tickets
set account_num = -1;  
delete from Reserves; 
select * from Waitlist; 

select name, username from Person where account_num in  
(select account_num from Waitlist where flight_number = 9); 


select * from Airport; 
select * from Aircraft; 
insert into Aircraft (aircraft_id) values(199); 

update Aircraft set aircraft_id = 1999 where aircraft_id = 199; 


select * from Person; 
select * from Flight_Operates; 
select * from Tickets; 
select * from Airport; 

insert into Flight_Operates(aircraft_id, airline_id, 
airport_id, flight_number, 
flight_type, depart_time, arrival_time, 
fare_first, fare_economy, departure_airport,
destination_airport,fare_business, 
num_stops, depart_date, return_date)
Values (6, 'UN', 'LAX', 91,'o', '04:10:00', '11:00:00', 
15.20, 16.30, 'LAX', 'EWR', 17.00, 0, '1998-07-21', '1998-07-22');

delete from Flight_Operates where flight_number = 999; 
            
	
            
insert into Flight_Operates(aircraft_id, airline_id, airport_id, flight_number, flight_type, depart_time, arrival_time, fare_first, fare_economy, departure_airport, destination_airport, fare_business, num_stops, depart_date, return_date) Values (1999,'UN','LAX',333,'One-Way','04:10:00','null',76.25,25.43,'LAX','EWR',50.00,1,'null','1998-07-23');

insert into Flight_Operates(aircraft_id, airline_id, airport_id, flight_number, flight_type, depart_time, arrival_time, fare_first, fare_economy, departure_airport, destination_airport, fare_business, num_stops, depart_date, return_date) Values (22,'UN','LAX',300,'Round-Trip','01:10:00','null',90.99,55.00,'LAX','EWR',75.00,2,'null','1998-07-23');

insert into Flight_Operates(aircraft_id, airline_id, airport_id, flight_number, flight_type, depart_time, arrival_time, fare_first, fare_economy, departure_airport, destination_airport, fare_business, num_stops, depart_date, return_date) Values (22,'EY','LGA',999,'Round-Trip','04:10:00','07:17:00',90.99,25.43,'LGA','LAX',50.00,0,'1998-07-22','1998-07-23');









select * from Flight_Operates; 

update Flight_Operates set fare_first = 77.77 where airline_id = 'UN' and flight_number = 91; 



