drop database if exists trafic;
create database if not exists trafic ;
use trafic;


drop table DATE_TRAFIC;
drop table STRAZI;

create table STRAZI(
id int unique auto_increment primary key,
tara char(10),
localitate varchar(15),
strada char(20),
nr_benzi_circulatie int,
latitudine_gps varchar(20),
longitudine_gps varchar(20));

create table CONDITII_METEO(
id int unique auto_increment primary key,
descriere varchar(40));

create table PERIOADA_ZI(
id int unique auto_increment primary key,
descriere varchar(40));

create table STAREA_DRUMULUI(
id int unique auto_increment primary key,
descriere varchar(40));


create table DATE_TRAFIC(
id int unique auto_increment primary key,
id_strada int,
id_cond_meteo int,
id_perioada_zi int,
id_starea_drumului int,
data_ora varchar(20),
nr_mediu_autovehicole int,
viteza_medie_autovehicole double,
nr_client int,
foreign key(id_STRADA) references STRAZI(id),
foreign key (id_cond_meteo) references CONDITII_METEO(id),
foreign key (id_perioada_zi) references PERIOADA_ZI(id),
foreign key (id_starea_drumului) references STAREA_DRUMULUI(id)
);



insert into STRAZI values
(1,'Romania','Cluj-Napoca','Motilor',4,24.8765,45.7654),
(2,'Romania','Cluj-Napoca','Clinicilor',2,26.8765,48.7654),
(3,'Romania','Cluj-Napoca','Mihai Eminescu',4,34.8765,44.7654),
(4,'Romania','Cluj-Napoca','George Cosbuc',3,32.8765,45.7654),
(5,'Romania','Cluj-Napoca','Iuliu Hossu',4,54.8765,23.7654),
(6,'Romania','Cluj-Napoca','Iuliu Hatieganu',4,25.8765,35.7654);

insert into CONDITII_METEO values
(1,'Ploaie'),
(2,'Insorit'),
(3,'Polei'),
(4,'Ninsoare'),
(5,'Grindina'),
(6,'Lapovita');

insert into PERIOADA_ZI values
(1,'Dimineata'),
(2,'Amiaz'),
(3,'Dupa-amiaz'),
(4,'Seara');

insert into STAREA_DRUMULUI values
(1,'Aglomarat'),
(2,'Partial aglomerat'),
(3,'Liber'),
(4,'In constructii');

insert into DATE_TRAFIC values
(1,1,4,1,2,'10.01.2015 10:20',100,50,1),
(2,1,2,2,1,'15.01.2015 13:30',300,43,2),
(3,1,6,3,1,'20.01.2015 17:00',700, 32,4);

select strazi.id from strazi where strazi.strada="Motilor";

select date_trafic.viteza_medie_autovehicole, date_trafic.nr_client from date_trafic where date_trafic.id_strada = 1 and  date_trafic.id_cond_meteo = 4 and date_trafic.id_perioada_zi =1 and date_trafic.id_starea_drumului =2;
select * from date_trafic;

update DATE_TRAFIC set viteza_medie_autovehicole= 50,nr_client=2 where date_trafic.id_strada = 1 and  date_trafic.id_cond_meteo = 4 and date_trafic.id_perioada_zi =1 and date_trafic.id_starea_drumului =2;


DROP PROCEDURE IF EXISTS ACTUALIZARE_CADOURI;

DELIMITER //
CREATE PROCEDURE ACTUALIZARE(id_str int, id_cond int, id_per int, id_stare int, viteza int, nr int)

  BEGIN
    START TRANSACTION;

            UPDATE date_trafic SET date_trafic.viteza_medie_autovehicole = viteza, date_trafic.nr_client=nr
            WHERE
            date_trafic.id_strada=id_str and date_trafic.id_cond_meteo=id_cond and date_trafic.id_perioada_zi=id_per and date_trafic.id_starea_drumului=id_stare;
   			COMMIT;
  END //
DELIMITER ;

call ACTUALIZARE (1,4,1,2,50,2);

select date_trafic.viteza_medie_autovehicole, date_trafic.nr_client from date_trafic where date_trafic.id_strada =1 and  date_trafic.id_cond_meteo = 2 and date_trafic.id_perioada_zi =3 and date_trafic.id_starea_drumului =4;
