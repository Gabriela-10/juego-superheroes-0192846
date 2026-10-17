import java.util.Random;
import java.util.Scanner;

// estadisticas base del personaje
abstract class Personaje {
    private static final int PORCENTAJE_TOTAL = 100;
    private static final int PORCENTAJE_PROTEGIDO = 50;
    private static final int CURACION_BASE = 20;
    private static final int VIDA_MINIMA = 0;
    private static final int MULTIPLICADOR_NORMAL = 1;

    private final String nombre;
    private final int vidaMaxima;
    private int vidaHp;
    private boolean defendiendo;
    protected int fuerza;
    protected int velocidad;

    public Personaje(String nombre, int fuerza, int velocidad, int vidaHp) {
        this.nombre = nombre;
        this.fuerza = fuerza;
        this.velocidad = velocidad;
        this.vidaHp = vidaHp;
        this.vidaMaxima = vidaHp;
        this.defendiendo = false;
    }

    public String getNombre() {
        return nombre;
    }

    public int getVidaHp() {
        return vidaHp;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public boolean estaVivo() {
        return vidaHp > VIDA_MINIMA;
    }

    // Ataque normal
    public void atacar(Personaje oponente) {
        atacar(oponente, MULTIPLICADOR_NORMAL);
    }

    // Sobrecarga del ataque: permite potenciar el daño con un multiplicador
    public void atacar(Personaje oponente, int multiplicador) {
        int danio = fuerza * multiplicador;
        System.out.println(nombre + " ataca a " + oponente.nombre + ".");
        oponente.recibirDanio(danio);
    }

    // Método para recibir daño, considerando si el personaje está defendiendo o no
    public void recibirDanio(int danio) {
        if (defendiendo) {
            danio -= danio * PORCENTAJE_PROTEGIDO / PORCENTAJE_TOTAL;
            defendiendo = false;
            System.out.println(nombre + " se defendió y reduce el daño.");
        }
        vidaHp = Math.max(VIDA_MINIMA, vidaHp - danio);
        System.out.println(nombre + " recibe " + danio + " de daño. Vida: " + vidaHp + "/" + vidaMaxima);
    }

    // se defiende para reducir el daño del próximo ataque
    public void defender() {
        defendiendo = true;
        System.out.println(nombre + " se prepara para defenderse (protege el "
                + PORCENTAJE_PROTEGIDO + "% del próximo daño).");
    }

    //se cura 20 puntos de vida 
    public void recuperarse() {
        vidaHp = Math.min(vidaMaxima, vidaHp + CURACION_BASE);
        System.out.println(nombre + " se recupera. Vida: " + vidaHp + "/" + vidaMaxima);
    }

    //se muestran las estadísticas del personaje
    public void mostrarEstadisticas() {
        System.out.println("[" + nombre + "] Fuerza: " + fuerza
                + " | Velocidad: " + velocidad
                + " | HP: " + vidaHp + "/" + vidaMaxima);
    }
}

class SuperHero extends Personaje {
    private static final int MULTIPLICADOR_ESPECIAL = 3;
    private static final int BONO_PODER = 5;

    public SuperHero(String nombre, int fuerza, int velocidad, int vidaHp) {
        super(nombre, fuerza, velocidad, vidaHp);
    }

    public void ataqueEspecial(Personaje oponente) {
        System.out.println(getNombre() + " usa su ATAQUE ESPECIAL!");
        atacar(oponente, MULTIPLICADOR_ESPECIAL);
    }

    //se aumenta las estadísticas del héroe en 5 puntos de fuerza y velocidad
    public void aumentarPoderes() {
        fuerza += BONO_PODER;
        velocidad += BONO_PODER;
        System.out.println(getNombre() + " aumenta sus poderes (+" + BONO_PODER + " fuerza y velocidad).");
    }
}

class Villano extends Personaje {
    private static final Random RANDOM = new Random();
    private static final int PORCENTAJE_TOTAL = 100;
    private static final int PROBABILIDAD_TRAMPA = 30;
    private static final int MULTIPLICADOR_TRAMPA = 2;

    public Villano(String nombre, int fuerza, int velocidad, int vidaHp) {
        super(nombre, fuerza, velocidad, vidaHp);
    }

    //el villano decide q hacer basado en una probabilidad
    public boolean decidir(int probabilidad) {
        return RANDOM.nextInt(PORCENTAJE_TOTAL) < probabilidad;
    }

    @Override
    public void atacar(Personaje oponente) {
        if (decidir(PROBABILIDAD_TRAMPA)) {
            System.out.println(getNombre() + " hace trampa y duplica su ataque!");
            atacar(oponente, MULTIPLICADOR_TRAMPA);
        } else {
            super.atacar(oponente);
        }
    }
}

public class App {
    // Opciones del menú
    private static final int OPCION_ATACAR = 1;
    private static final int OPCION_ESPECIAL = 2;
    private static final int OPCION_DEFENDER = 3;
    private static final int OPCION_AUMENTAR = 4;
    private static final int OPCION_RECUPERAR = 5;
    private static final int OPCION_ESTADISTICAS = 6;
    private static final int OPCION_INVALIDA = -1;

    // Configuración de los personajes
    private static final String NOMBRE_HEROE = "Oracle Man";
    private static final int FUERZA_HEROE = 20;
    private static final int VELOCIDAD_HEROE = 15;
    private static final int VIDA_HEROE = 100;

    private static final String NOMBRE_VILLANO = "Doctor Bug";
    private static final int FUERZA_VILLANO = 18;
    private static final int VELOCIDAD_VILLANO = 12;
    private static final int VIDA_VILLANO = 100;

    // Comportamiento del villano
    private static final int PORCENTAJE_TOTAL = 100;
    private static final int UMBRAL_VIDA_BAJA = 30;
    private static final int PROBABILIDAD_CURAR = 40;
    private static final int PROBABILIDAD_DEFENDER = 20;

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        SuperHero heroe = new SuperHero(NOMBRE_HEROE, FUERZA_HEROE, VELOCIDAD_HEROE, VIDA_HEROE);
        Villano villano = new Villano(NOMBRE_VILLANO, FUERZA_VILLANO, VELOCIDAD_VILLANO, VIDA_VILLANO);

        heroe.mostrarEstadisticas();
        villano.mostrarEstadisticas();

        int ronda = 1;
        while (heroe.estaVivo() && villano.estaVivo()) {
            System.out.println("\n===== Ronda " + ronda + " =====");

            // El más veloz actúa primero
            if (heroe.getVelocidad() >= villano.getVelocidad()) {
                turnoHeroe(heroe, villano);
                if (villano.estaVivo()) {
                    turnoVillano(villano, heroe);
                }
            } else {
                turnoVillano(villano, heroe);
                if (heroe.estaVivo()) {
                    turnoHeroe(heroe, villano);
                }
            }
            ronda++;
        }

        Personaje ganador = heroe.estaVivo() ? heroe : villano;
        System.out.println("\n¡" + ganador.getNombre() + " gana la batalla!");
        SCANNER.close();
    }

    private static void turnoHeroe(SuperHero heroe, Villano villano) {
        boolean turnoUsado = false;
        while (!turnoUsado) {
            mostrarMenu(heroe);
            turnoUsado = true;
            switch (leerOpcion()) {
                case OPCION_ATACAR: heroe.atacar(villano);
                    break;
                case OPCION_ESPECIAL: heroe.ataqueEspecial(villano);
                    break;
                case OPCION_DEFENDER: heroe.defender();
                    break;
                case OPCION_AUMENTAR: heroe.aumentarPoderes();
                    break;
                case OPCION_RECUPERAR: heroe.recuperarse();
                    break;
                case OPCION_ESTADISTICAS: {
                    heroe.mostrarEstadisticas();
                    villano.mostrarEstadisticas();
                    turnoUsado = false; // Ver estadísticas no consume el turno
                }
                default: {
                    System.out.println("Opción no válida, intenta de nuevo.");
                    turnoUsado = false;
                }
            }
        }
    }

    private static void turnoVillano(Villano villano, SuperHero heroe) {
        System.out.println("\n--- Turno de " + villano.getNombre() + " ---");
        boolean vidaBaja = villano.getVidaHp() * PORCENTAJE_TOTAL / villano.getVidaMaxima() < UMBRAL_VIDA_BAJA;

        if (vidaBaja && villano.decidir(PROBABILIDAD_CURAR)) {
            villano.recuperarse();
        } else if (villano.decidir(PROBABILIDAD_DEFENDER)) {
            villano.defender();
        } else {
            villano.atacar(heroe);
        }
    }

    private static void mostrarMenu(SuperHero heroe) {
        System.out.println("\n--- Turno de " + heroe.getNombre() + " ---");
        System.out.println(OPCION_ATACAR + ". Atacar");
        System.out.println(OPCION_ESPECIAL + ". Ataque especial");
        System.out.println(OPCION_DEFENDER + ". Defender");
        System.out.println(OPCION_AUMENTAR + ". Aumentar poderes");
        System.out.println(OPCION_RECUPERAR + ". Recuperarse");
        System.out.println(OPCION_ESTADISTICAS + ". Mostrar estadísticas");
        System.out.print("Elige una opción: ");
    }

    private static int leerOpcion() {
        try {
            return Integer.parseInt(SCANNER.nextLine().trim());
        } catch (NumberFormatException e) {
            return OPCION_INVALIDA;
        }
    }
}