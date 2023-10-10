package electre1_d;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Agregation {

    public static double[][] MatriceConcordance(double[] t_poids, double[][] t_pref) {
        double m = 0, v = 0, vc = 0;
        double c[][] = new double[t_pref.length][t_pref.length];
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        for (int i = 0; i < t_poids.length; i++) {
            m = m + t_poids[i];
        }
        //MATRICE DE CONCORDENCE
        for (int i = 0; i < t_pref.length; i++) {
            for (int h = 0; h < t_pref.length; h++) {
                for (int j = 0; j < t_poids.length; j++) {
                    if (t_pref[i][j] >= t_pref[h][j]) {
                        v = v + t_poids[j];
                    } else {
                        v = v + 0;
                    }
                }

                if (i == h) {
                    c[i][h] = 0;
                } else {
                    vc = v / m;
                }
                c[i][h] = Double.parseDouble(decimalFormat.format(vc));
                vc = 0;
                v = 0;
            }
        }
        return c;
    }

    public static double[][] MatriceDiscordonce(double[] t_poids, double[][] t_pref) {
        double pmax = 0, max = 0;
        double d[][] = new double[t_pref.length][t_pref.length];
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        //MATRICE DE DISCORDANCE
        for (int i = 0; i < t_pref.length; i++) {
            for (int j = 0; j < t_poids.length; j++) {
                if (max <= t_pref[i][j]) {
                    max = t_pref[i][j];
                }
            }
        }
        for (int i = 0; i < t_pref.length; i++) {
            for (int h = 0; h < t_pref.length; h++) {
                for (int j = 0; j < t_poids.length; j++) {
                    double a = t_pref[h][j] - t_pref[i][j];
                    if (pmax <= a) {
                        pmax = a;
                    }
                }
                d[i][h] = Double.parseDouble(decimalFormat.format(((1 / max) * pmax)));
                pmax = 0;
                if (i == h) {
                    d[i][h] = 0;
                }
            }
        }
        return d;
    }

    public static int[][] sommet_sommet(double c[][], double d[][], double a, double b) {
        int size = c.length;
        int mat[][] = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    mat[i][j] = 0;
                } else if (c[i][j] >= a && d[i][j] <= b) {
                    mat[i][j] = 1;
                }
            }
        }
        return mat;
    }

    public static String Exploitation(int mat[][]) {
        ArrayList<Integer> AltS = new ArrayList<>();
        int s = 0;
        for (int j = 0; j < mat.length; j++) {
            s = 0;
            for (int i = 0; i < mat.length; i++) {
                s = s + mat[i][j];
                if (i == mat.length - 1 && s == 0) {
                    AltS.add(j+1);
                }    
            }
        }
        return "les Projets sélectionnées sont : " + AltS;
    }
}
