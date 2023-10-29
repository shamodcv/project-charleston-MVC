CREATE DATABASE Charleston;

USE Charleston;

CREATE TABLE Admin(
                      UserName VARCHAR(15) NOT NULL,
                      Password VARCHAR(15) NOT NULL
);


CREATE TABLE Guest(
                      Guest_ID_type VARCHAR(30),
                      Guest_ID VARCHAR(30) NOT NULL,
                      Guest_Name VARCHAR(30),
                      Guest_Contact_No VARCHAR(20),
                      Guest_Country VARCHAR(30),
                      Guest_Email VARCHAR(30),
                      CONSTRAINT PRIMARY KEY (Guest_ID)
);

CREATE TABLE Room(

                     Room_ID VARCHAR(6) NOT NULL ,
                     Room_Type VARCHAR(30),
                     Room_Details VARCHAR(50),
                     Number_OF_Beds VARCHAR(10),
                     Room_price DECIMAL(10,2),
                     CONSTRAINT PRIMARY KEY (Room_ID)

);

CREATE TABLE Tour(

                     Tour_ID VARCHAR(20) NOT NULL,
                     Tour_Name VARCHAR(30),
                     Tour_Description VARCHAR(40),
                     Price DECIMAL(10,2),
                     CONSTRAINT PRIMARY KEY (Tour_ID)

);


CREATE TABLE Tour_Details(
                             Book_ID VARCHAR(30) NOT NULL,
                             Guest_ID VARCHAR(30) NOT NULL,
                             Guest_Name VARCHAR(30),
                             Tour_ID VARCHAR(20) NOT NULL,
                             Tour_Name VARCHAR(30),
                             Price DECIMAL(10,2),
                             CONSTRAINT PRIMARY KEY (Book_ID),
                             CONSTRAINT FOREIGN KEY (Guest_ID) REFERENCES Guest (Guest_ID) ON Delete Cascade On update cascade,
                             CONSTRAINT FOREIGN KEY (Tour_ID) REFERENCES Tour (Tour_ID)ON Delete Cascade On update cascade
);


CREATE TABLE Resavations(

                            Res_ID VARCHAR(20) NOT NULL,
                            Guest_ID VARCHAR(30) NOT NULL,
                            Room_ID VARCHAR(20) NOT NULL,
                            Room_price DECIMAL(10,2),
                            Check_in_Date DATE NOT NULL,
                            Check_out_Date DATE,
                            CONSTRAINT PRIMARY KEY (Res_ID),
                            CONSTRAINT FOREIGN KEY (Guest_ID) REFERENCES Guest(Guest_ID) ON Delete Cascade On update cascade,
                            CONSTRAINT FOREIGN KEY (Room_ID) REFERENCES Room(Room_ID) ON Delete Cascade On update cascade


);


CREATE TABLE Food_Menu(

                          Meal_Type VARCHAR(40),
                          Meal_No VARCHAR(40) NOT NULL,
                          Meal_Name VARCHAR(50),
                          Meal_Discription VARCHAR(100),
                          Meal_Price DECIMAL(10,2),
                          CONSTRAINT PRIMARY KEY (Meal_No)


);

CREATE TABLE Resavations_detail(
                                   Res_ID VARCHAR(20) NOT NULL,
                                   Room_ID VARCHAR(6) NOT NULL,
                                   Room_price VARCHAR(50),
                                   Guest_ID VARCHAR(30) NOT NULL,
                                   Guest_Name VARCHAR(30),
                                   Check_In_Date DATE NOT NULL,
                                   Check_Out_Date DATE,
                                   CONSTRAINT pk_Reservations_Detail PRIMARY KEY (Res_ID),
                                   CONSTRAINT fk_Res_ID_Reservations_Detail FOREIGN KEY (Res_ID) REFERENCES Resavations (Res_ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Orders(
                       orderId VARCHAR(6) NOT NULL,
                       date DATE,
                       Res_ID VARCHAR(6) NOT NULL,
                       CONSTRAINT PRIMARY KEY (orderId),
                       CONSTRAINT FOREIGN KEY(Res_ID) REFERENCES Resavations(Res_ID) on Delete Cascade on Update Cascade
);

CREATE TABLE OrderDetail(
                            orderId VARCHAR(6) NOT NULL,
                            Meal_No VARCHAR(6) NOT NULL,
                            qty INT(11),
                            Ammount DECIMAL(8,2),
                            CONSTRAINT PRIMARY KEY (orderId),
                            CONSTRAINT FOREIGN KEY (orderId) REFERENCES Orders(orderId) on Delete Cascade on Update Cascade,
                            CONSTRAINT FOREIGN KEY (Meal_No) REFERENCES Food_Menu(Meal_No) on Delete Cascade on Update Cascade
);

CREATE TABLE Payment(
                        PayID VARCHAR(20) NOT NULL,
                        Guest_ID VARCHAR(30) NOT NULL,
                        Guest_Name VARCHAR(30),
                        Res_ID VARCHAR(6) NOT NULL,
                        Room_ID VARCHAR(6) NOT NULL ,
                        Check_in_Date DATE NOT NULL,
                        Check_out_Date DATE  NOT NULL,
                        Orders_Ammount DECIMAL(10,2),
                        Total_Price DECIMAL(10,2),
                        CONSTRAINT PRIMARY KEY (PayID),
                        CONSTRAINT FOREIGN KEY (Guest_ID) REFERENCES Guest (Guest_ID) ON Delete Cascade On update cascade,
                        CONSTRAINT FOREIGN KEY (Res_ID)  REFERENCES Resavations (Res_ID) ON Delete Cascade On update cascade,
                        CONSTRAINT FOREIGN KEY (Room_ID) REFERENCES Room (Room_ID) ON Delete Cascade On update cascade

);