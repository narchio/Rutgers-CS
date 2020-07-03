CREATE TABLE IF NOT EXISTS Airline(
	airline_id varchar(2) NOT NULL, 
    primary key (airline_id)
); 

CREATE TABLE IF NOT EXISTS Aircraft(
	aircraft_id int NOT NULL, 
    primary key (aircraft_id)
); 

CREATE TABLE IF NOT EXISTS Airport(
	airport_id varchar(3) NOT NULL, 
    primary key (airport_id)
); 

CREATE TABLE IF NOT EXISTS Owns(
	aircraft_id int NOT NULL, 
    airline_id varchar(2) NOT NULL, 
    primary key (aircraft_id), 
    foreign key (aircraft_id) references Aircraft(aircraft_id), 
    foreign key (airline_id) references Airline(airline_id)
); 

CREATE TABLE IF NOT EXISTS Has_Seats(
	aircraft_id int NOT NULL, 
    seat_number int NOT NULL, 
    seat_class varchar(1), 
    primary key (aircraft_id, seat_number), 
    foreign key (aircraft_id) references Aircraft(aircraft_id)
); 

CREATE TABLE IF NOT EXISTS Flight_Operates(
	aircraft_id int NOT NULL, 
    airline_id varchar(2) NOT NULL,
	airport_id varchar(3) NOT NULL, 
    flight_number int NOT NULL, 
    flight_type varchar(1), 
    depart_time datetime, 
    arive_time datetime, 
    fare_first float, 
    fare_economy float, 
    departure_airport varchar(3), 
    destination_airport varchar(3), 
    primary key (airline_id, airport_id, aircraft_id, flight_number), 
    foreign key (aircraft_id) references Aircraft(aircraft_id), 
    foreign key (airline_id) references Airline(airline_id),
    foreign key (airport_id) references Airport(airport_id)
); 

-- CREATE TABLE IF NOT EXISTS DayOfTheWeek(
-- 	day_number int NOT NULL, 
--     day_of_week varchar(10), 
--     primary key (day_number)
-- );

CREATE TABLE IF NOT EXISTS Tickets(
	ticket_number int NOT NULL, 
    round_trip boolean, 
    booking_fee float, 
    issue_date datetime, 
    total_fare float, 
    primary key (ticket_number)
); 

CREATE TABLE IF NOT EXISTS Person(
	username varchar(15) NOT NULL, 
    password varchar(15) NOT NULL, 
    name varchar(50) NOT NULL, 
    admin boolean NOT NULL, 
    is_customer_rep boolean NOT NULL, 
    is_customer boolean NOT NULL, 
    account_num int NOT NULL, 
    primary key (username)
); 

CREATE TABLE IF NOT EXISTS Reserves(
	ticket_number int NOT NULL, 
    username varchar(15) NOT NULL, 
    primary key (ticket_number), 
    foreign key (username) references Person(username), 
    foreign key (ticket_number) references Tickets(ticket_number)
); 

CREATE TABLE IF NOT EXISTS Flights_On(
	airline_id varchar(2) NOT NULL, 
    airport_id varchar(3) NOT NULL, 
    aircraft_id int NOT NULL, 
    flight_number int NOT NULL, 
    day_number int NOT NULL, 
    primary key (airline_id, airport_id, aircraft_id, flight_number, day_number), 
    foreign key (airline_id, airport_id, aircraft_id, flight_number) references Flight_Operates(airline_id, airport_id, aircraft_id, flight_number), 
    foreign key (day_number) references DayOfTheWeek(day_number)
);

CREATE TABLE IF NOT EXISTS Trip(
	ticket_number int NOT NULL, 
    airport_id varchar(3) NOT NULL, 
    airline_id varchar(2) NOT NULL, 
    aircraft_id int NOT NULL, 
    seat_number int NOT NULL, 
    flight_number int NOT NULL, 
    date_of_trip datetime NOT NULL, 
    meal varchar(30), 
    primary key(ticket_number, airport_id, airline_id, aircraft_id, seat_number, flight_number), 
    foreign key (ticket_number) references Tickets(ticket_number), 
    foreign key (airline_id, airport_id, aircraft_id, flight_number) references Flight_Operates(airline_id, airport_id, aircraft_id, flight_number), 
	foreign key (aircraft_id, seat_number) references Has_Seats(aircraft_id, seat_number)
); 
 





