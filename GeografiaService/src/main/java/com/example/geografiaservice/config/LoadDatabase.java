package com.example.geografiaservice.config;

import com.example.geografiaservice.model.Ciudad;
import com.example.geografiaservice.model.Comuna;
import com.example.geografiaservice.model.Region;
import com.example.geografiaservice.repository.CiudadRepository;
import com.example.geografiaservice.repository.ComunaRepository;
import com.example.geografiaservice.repository.RegionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(RegionRepository regionRepo, CiudadRepository ciudadRepo, 
                                    ComunaRepository comunaRepo) {
        return args -> {

            if (regionRepo.count() == 0 && ciudadRepo.count() == 0 && comunaRepo.count() == 0) {

                // ==================== CARGAR LAS 16 REGIONES DE CHILE ====================
                Region aricaParinacota = new Region(null, "Región de Arica y Parinacota", "XV", 1, null, null);
                Region tarapaca = new Region(null, "Región de Tarapacá", "I", 2, null, null);
                Region antofagasta = new Region(null, "Región de Antofagasta", "II", 3, null, null);
                Region atacama = new Region(null, "Región de Atacama", "III", 4, null, null);
                Region coquimbo = new Region(null, "Región de Coquimbo", "IV", 5, null, null);
                Region valparaiso = new Region(null, "Región de Valparaíso", "V", 6, null, null);
                Region metropolitana = new Region(null, "Región Metropolitana de Santiago", "RM", 7, null, null);
                Region ohiggins = new Region(null, "Región del Libertador General Bernardo O'Higgins", "VI", 8, null, null);
                Region maule = new Region(null, "Región del Maule", "VII", 9, null, null);
                Region nuble = new Region(null, "Región de Ñuble", "XVI", 10, null, null);
                Region biobio = new Region(null, "Región del Biobío", "VIII", 11, null, null);
                Region araucania = new Region(null, "Región de La Araucanía", "IX", 12, null, null);
                Region rios = new Region(null, "Región de Los Ríos", "XIV", 13, null, null);
                Region lagos = new Region(null, "Región de Los Lagos", "X", 14, null, null);
                Region aysen = new Region(null, "Región de Aysén del General Carlos Ibáñez del Campo", "XI", 15, null, null);
                Region magallanes = new Region(null, "Región de Magallanes y de la Antártica Chilena", "XII", 16, null, null);

                regionRepo.saveAll(List.of(aricaParinacota, tarapaca, antofagasta, atacama, coquimbo,
                                          valparaiso, metropolitana, ohiggins, maule, nuble, biobio,
                                          araucania, rios, lagos, aysen, magallanes));

                // ==================== CARGAR CIUDADES ====================
                
                // XV - Arica y Parinacota
                Ciudad arica = new Ciudad(null, "Arica", true, aricaParinacota, null);
                Ciudad putre = new Ciudad(null, "Putre", false, aricaParinacota, null);

                // I - Tarapacá
                Ciudad iquique = new Ciudad(null, "Iquique", true, tarapaca, null);
                Ciudad altoHospicio = new Ciudad(null, "Alto Hospicio", false, tarapaca, null);

                // II - Antofagasta
                Ciudad antofagastaCiudad = new Ciudad(null, "Antofagasta", true, antofagasta, null);
                Ciudad calama = new Ciudad(null, "Calama", false, antofagasta, null);
                Ciudad tocopilla = new Ciudad(null, "Tocopilla", false, antofagasta, null);

                // III - Atacama
                Ciudad copiapo = new Ciudad(null, "Copiapó", true, atacama, null);
                Ciudad caldera = new Ciudad(null, "Caldera", false, atacama, null);
                Ciudad vallenar = new Ciudad(null, "Vallenar", false, atacama, null);

                // IV - Coquimbo
                Ciudad laSerena = new Ciudad(null, "La Serena", true, coquimbo, null);
                Ciudad coquimboCiudad = new Ciudad(null, "Coquimbo", false, coquimbo, null);
                Ciudad ovalle = new Ciudad(null, "Ovalle", false, coquimbo, null);

                // V - Valparaíso
                Ciudad valparaisoCiudad = new Ciudad(null, "Valparaíso", true, valparaiso, null);
                Ciudad vinadelMar = new Ciudad(null, "Viña del Mar", false, valparaiso, null);
                Ciudad quilpue = new Ciudad(null, "Quilpué", false, valparaiso, null);
                Ciudad sanAntonio = new Ciudad(null, "San Antonio", false, valparaiso, null);
                Ciudad quillota = new Ciudad(null, "Quillota", false, valparaiso, null);

                // RM - Metropolitana
                Ciudad santiago = new Ciudad(null, "Santiago", true, metropolitana, null);
                Ciudad maipu = new Ciudad(null, "Maipú", false, metropolitana, null);
                Ciudad puenteAlto = new Ciudad(null, "Puente Alto", false, metropolitana, null);
                Ciudad laFlorida = new Ciudad(null, "La Florida", false, metropolitana, null);
                Ciudad sanBernardo = new Ciudad(null, "San Bernardo", false, metropolitana, null);

                // VI - O'Higgins
                Ciudad rancagua = new Ciudad(null, "Rancagua", true, ohiggins, null);
                Ciudad sanFernando = new Ciudad(null, "San Fernando", false, ohiggins, null);
                Ciudad santaCruz = new Ciudad(null, "Santa Cruz", false, ohiggins, null);

                // VII - Maule
                Ciudad talca = new Ciudad(null, "Talca", true, maule, null);
                Ciudad curico = new Ciudad(null, "Curicó", false, maule, null);
                Ciudad linares = new Ciudad(null, "Linares", false, maule, null);

                // XVI - Ñuble
                Ciudad chillan = new Ciudad(null, "Chillán", true, nuble, null);
                Ciudad sanCarlos = new Ciudad(null, "San Carlos", false, nuble, null);

                // VIII - Biobío
                Ciudad concepcion = new Ciudad(null, "Concepción", true, biobio, null);
                Ciudad talcahuano = new Ciudad(null, "Talcahuano", false, biobio, null);
                Ciudad losAngeles = new Ciudad(null, "Los Ángeles", false, biobio, null);

                // IX - Araucanía
                Ciudad temuco = new Ciudad(null, "Temuco", true, araucania, null);
                Ciudad villarrica = new Ciudad(null, "Villarrica", false, araucania, null);
                Ciudad pucon = new Ciudad(null, "Pucón", false, araucania, null);

                // XIV - Los Ríos
                Ciudad valdivia = new Ciudad(null, "Valdivia", true, rios, null);
                Ciudad riobueno = new Ciudad(null, "Río Bueno", false, rios, null);

                // X - Los Lagos
                Ciudad puertoMontt = new Ciudad(null, "Puerto Montt", true, lagos, null);
                Ciudad osorno = new Ciudad(null, "Osorno", false, lagos, null);
                Ciudad castro = new Ciudad(null, "Castro", false, lagos, null);

                // XI - Aysén
                Ciudad coyhaique = new Ciudad(null, "Coyhaique", true, aysen, null);
                Ciudad puertoAysen = new Ciudad(null, "Puerto Aysén", false, aysen, null);

                // XII - Magallanes
                Ciudad puntaArenas = new Ciudad(null, "Punta Arenas", true, magallanes, null);
                Ciudad puertoNatales = new Ciudad(null, "Puerto Natales", false, magallanes, null);

                ciudadRepo.saveAll(List.of(
                    arica, putre, iquique, altoHospicio, antofagastaCiudad, calama, tocopilla,
                    copiapo, caldera, vallenar, laSerena, coquimboCiudad, ovalle,
                    valparaisoCiudad, vinadelMar, quilpue, sanAntonio, quillota,
                    santiago, maipu, puenteAlto, laFlorida, sanBernardo,
                    rancagua, sanFernando, santaCruz, talca, curico, linares,
                    chillan, sanCarlos, concepcion, talcahuano, losAngeles,
                    temuco, villarrica, pucon, valdivia, riobueno,
                    puertoMontt, osorno, castro, coyhaique, puertoAysen,
                    puntaArenas, puertoNatales
                ));

                // ==================== CARGAR COMUNAS ====================

                // XV - Arica y Parinacota
                Comuna aricaComuna = new Comuna(null, "Arica", aricaParinacota, arica);
                Comuna camarones = new Comuna(null, "Camarones", aricaParinacota, arica);
                Comuna putreComuna = new Comuna(null, "Putre", aricaParinacota, putre);
                Comuna generalLagos = new Comuna(null, "General Lagos", aricaParinacota, putre);

                // I - Tarapacá
                Comuna iquiqueComuna = new Comuna(null, "Iquique", tarapaca, iquique);
                Comuna altoHospicioComuna = new Comuna(null, "Alto Hospicio", tarapaca, altoHospicio);
                Comuna pozo = new Comuna(null, "Pozo Almonte", tarapaca, iquique);
                Comuna pica = new Comuna(null, "Pica", tarapaca, iquique);

                // II - Antofagasta
                Comuna antofagastaComuna = new Comuna(null, "Antofagasta", antofagasta, antofagastaCiudad);
                Comuna mejillones = new Comuna(null, "Mejillones", antofagasta, antofagastaCiudad);
                Comuna calamaComuna = new Comuna(null, "Calama", antofagasta, calama);
                Comuna ollague = new Comuna(null, "Ollagüe", antofagasta, calama);
                Comuna sanPedroAtacama = new Comuna(null, "San Pedro de Atacama", antofagasta, calama);
                Comuna tocopillaComuna = new Comuna(null, "Tocopilla", antofagasta, tocopilla);
                Comuna mariaElena = new Comuna(null, "María Elena", antofagasta, tocopilla);

                // III - Atacama
                Comuna copiapoComuna = new Comuna(null, "Copiapó", atacama, copiapo);
                Comuna calderaComuna = new Comuna(null, "Caldera", atacama, caldera);
                Comuna tierraAmarilla = new Comuna(null, "Tierra Amarilla", atacama, copiapo);
                Comuna vallenarComuna = new Comuna(null, "Vallenar", atacama, vallenar);
                Comuna huasco = new Comuna(null, "Huasco", atacama, vallenar);
                Comuna altoDelCarmen = new Comuna(null, "Alto del Carmen", atacama, vallenar);

                // IV - Coquimbo
                Comuna laSerenaComuna = new Comuna(null, "La Serena", coquimbo, laSerena);
                Comuna coquimboComuna = new Comuna(null, "Coquimbo", coquimbo, coquimboCiudad);
                Comuna andacollo = new Comuna(null, "Andacollo", coquimbo, laSerena);
                Comuna vicuna = new Comuna(null, "Vicuña", coquimbo, laSerena);
                Comuna ovalleComuna = new Comuna(null, "Ovalle", coquimbo, ovalle);
                Comuna montePatria = new Comuna(null, "Monte Patria", coquimbo, ovalle);
                Comuna illapel = new Comuna(null, "Illapel", coquimbo, ovalle);

                // V - Valparaíso
                Comuna valparaisoComuna = new Comuna(null, "Valparaíso", valparaiso, valparaisoCiudad);
                Comuna casablanca = new Comuna(null, "Casablanca", valparaiso, valparaisoCiudad);
                Comuna vinadelMarComuna = new Comuna(null, "Viña del Mar", valparaiso, vinadelMar);
                Comuna conCon = new Comuna(null, "Concón", valparaiso, vinadelMar);
                Comuna quilpueComuna = new Comuna(null, "Quilpué", valparaiso, quilpue);
                Comuna villaAlemana = new Comuna(null, "Villa Alemana", valparaiso, quilpue);
                Comuna sanAntonioComuna = new Comuna(null, "San Antonio", valparaiso, sanAntonio);
                Comuna cartagena = new Comuna(null, "Cartagena", valparaiso, sanAntonio);
                Comuna quillotaComuna = new Comuna(null, "Quillota", valparaiso, quillota);
                Comuna laCalera = new Comuna(null, "La Calera", valparaiso, quillota);

                // RM - Metropolitana
                Comuna santiagoComuna = new Comuna(null, "Santiago", metropolitana, santiago);
                Comuna providencia = new Comuna(null, "Providencia", metropolitana, santiago);
                Comuna lasCondes = new Comuna(null, "Las Condes", metropolitana, santiago);
                Comuna nunoa = new Comuna(null, "Ñuñoa", metropolitana, santiago);
                Comuna vitacura = new Comuna(null, "Vitacura", metropolitana, santiago);
                Comuna laReina = new Comuna(null, "La Reina", metropolitana, santiago);
                Comuna macul = new Comuna(null, "Macul", metropolitana, santiago);
                Comuna penalolen = new Comuna(null, "Peñalolén", metropolitana, santiago);
                Comuna maipuComuna = new Comuna(null, "Maipú", metropolitana, maipu);
                Comuna cerrillos = new Comuna(null, "Cerrillos", metropolitana, maipu);
                Comuna puenteAltoComuna = new Comuna(null, "Puente Alto", metropolitana, puenteAlto);
                Comuna pirque = new Comuna(null, "Pirque", metropolitana, puenteAlto);
                Comuna laFloridaComuna = new Comuna(null, "La Florida", metropolitana, laFlorida);
                Comuna laPintana = new Comuna(null, "La Pintana", metropolitana, laFlorida);
                Comuna sanBernardoComuna = new Comuna(null, "San Bernardo", metropolitana, sanBernardo);
                Comuna calera = new Comuna(null, "Calera de Tango", metropolitana, sanBernardo);

                // VI - O'Higgins
                Comuna rancaguaComuna = new Comuna(null, "Rancagua", ohiggins, rancagua);
                Comuna machali = new Comuna(null, "Machalí", ohiggins, rancagua);
                Comuna graneros = new Comuna(null, "Graneros", ohiggins, rancagua);
                Comuna sanFernandoComuna = new Comuna(null, "San Fernando", ohiggins, sanFernando);
                Comuna chimbarongo = new Comuna(null, "Chimbarongo", ohiggins, sanFernando);
                Comuna santaCruzComuna = new Comuna(null, "Santa Cruz", ohiggins, santaCruz);
                Comuna pichilemu = new Comuna(null, "Pichilemu", ohiggins, santaCruz);

                // VII - Maule
                Comuna talcaComuna = new Comuna(null, "Talca", maule, talca);
                Comuna mauleMaule = new Comuna(null, "Maule", maule, talca);
                Comuna sanClemente = new Comuna(null, "San Clemente", maule, talca);
                Comuna curicoComuna = new Comuna(null, "Curicó", maule, curico);
                Comuna molina = new Comuna(null, "Molina", maule, curico);
                Comuna linaresComuna = new Comuna(null, "Linares", maule, linares);
                Comuna parral = new Comuna(null, "Parral", maule, linares);

                // XVI - Ñuble
                Comuna chillanComuna = new Comuna(null, "Chillán", nuble, chillan);
                Comuna chillanViejo = new Comuna(null, "Chillán Viejo", nuble, chillan);
                Comuna sanCarlosComuna = new Comuna(null, "San Carlos", nuble, sanCarlos);
                Comuna coihueco = new Comuna(null, "Coihueco", nuble, sanCarlos);

                // VIII - Biobío
                Comuna concepcionComuna = new Comuna(null, "Concepción", biobio, concepcion);
                Comuna sanPedro = new Comuna(null, "San Pedro de la Paz", biobio, concepcion);
                Comuna hualpen = new Comuna(null, "Hualpén", biobio, concepcion);
                Comuna talcahuanoComuna = new Comuna(null, "Talcahuano", biobio, talcahuano);
                Comuna penco = new Comuna(null, "Penco", biobio, talcahuano);
                Comuna losAngelesComuna = new Comuna(null, "Los Ángeles", biobio, losAngeles);
                Comuna mulchen = new Comuna(null, "Mulchén", biobio, losAngeles);

                // IX - Araucanía
                Comuna temucoComuna = new Comuna(null, "Temuco", araucania, temuco);
                Comuna padrelasCasas = new Comuna(null, "Padre las Casas", araucania, temuco);
                Comuna villarricaComuna = new Comuna(null, "Villarrica", araucania, villarrica);
                Comuna puconComuna = new Comuna(null, "Pucón", araucania, pucon);
                Comuna lautaro = new Comuna(null, "Lautaro", araucania, temuco);

                // XIV - Los Ríos
                Comuna valdiviaComuna = new Comuna(null, "Valdivia", rios, valdivia);
                Comuna panguipulli = new Comuna(null, "Panguipulli", rios, valdivia);
                Comuna riobuenoComuna = new Comuna(null, "Río Bueno", rios, riobueno);
                Comuna laUnion = new Comuna(null, "La Unión", rios, riobueno);

                // X - Los Lagos
                Comuna puertoMonttComuna = new Comuna(null, "Puerto Montt", lagos, puertoMontt);
                Comuna puertoVaras = new Comuna(null, "Puerto Varas", lagos, puertoMontt);
                Comuna osornoComuna = new Comuna(null, "Osorno", lagos, osorno);
                Comuna sanPablos = new Comuna(null, "San Pablo", lagos, osorno);
                Comuna castroComuna = new Comuna(null, "Castro", lagos, castro);
                Comuna ancud = new Comuna(null, "Ancud", lagos, castro);

                // XI - Aysén
                Comuna coyhaiquComuna = new Comuna(null, "Coyhaique", aysen, coyhaique);
                Comuna puertoAysenComuna = new Comuna(null, "Puerto Aysén", aysen, puertoAysen);
                Comuna chile = new Comuna(null, "Chile Chico", aysen, coyhaique);

                // XII - Magallanes
                Comuna puntaArenasComuna = new Comuna(null, "Punta Arenas", magallanes, puntaArenas);
                Comuna puertoNatalesComuna = new Comuna(null, "Puerto Natales", magallanes, puertoNatales);
                Comuna porvenir = new Comuna(null, "Porvenir", magallanes, puntaArenas);

                comunaRepo.saveAll(List.of(
                    // XV
                    aricaComuna, camarones, putreComuna, generalLagos,
                    // I
                    iquiqueComuna, altoHospicioComuna, pozo, pica,
                    // II
                    antofagastaComuna, mejillones, calamaComuna, ollague, sanPedroAtacama, tocopillaComuna, mariaElena,
                    // III
                    copiapoComuna, calderaComuna, tierraAmarilla, vallenarComuna, huasco, altoDelCarmen,
                    // IV
                    laSerenaComuna, coquimboComuna, andacollo, vicuna, ovalleComuna, montePatria, illapel,
                    // V
                    valparaisoComuna, casablanca, vinadelMarComuna, conCon, quilpueComuna, villaAlemana,
                    sanAntonioComuna, cartagena, quillotaComuna, laCalera,
                    // RM
                    santiagoComuna, providencia, lasCondes, nunoa, vitacura, laReina, macul, penalolen,
                    maipuComuna, cerrillos, puenteAltoComuna, pirque, laFloridaComuna, laPintana,
                    sanBernardoComuna, calera,
                    // VI
                    rancaguaComuna, machali, graneros, sanFernandoComuna, chimbarongo, santaCruzComuna, pichilemu,
                    // VII
                    talcaComuna, mauleMaule, sanClemente, curicoComuna, molina, linaresComuna, parral,
                    // XVI
                    chillanComuna, chillanViejo, sanCarlosComuna, coihueco,
                    // VIII
                    concepcionComuna, sanPedro, hualpen, talcahuanoComuna, penco, losAngelesComuna, mulchen,
                    // IX
                    temucoComuna, padrelasCasas, villarricaComuna, puconComuna, lautaro,
                    // XIV
                    valdiviaComuna, panguipulli, riobuenoComuna, laUnion,
                    // X
                    puertoMonttComuna, puertoVaras, osornoComuna, sanPablos, castroComuna, ancud,
                    // XI
                    coyhaiquComuna, puertoAysenComuna, chile,
                    // XII
                    puntaArenasComuna, puertoNatalesComuna, porvenir
                ));

                System.out.println("====================================");
                System.out.println("Datos geográficos de Chile cargados correctamente:");
                System.out.println("====================================");
            } else {
                System.out.println("⚠️  Ya existen datos en la base. No se cargó nada nuevo.");
            }

        };
    }
}
