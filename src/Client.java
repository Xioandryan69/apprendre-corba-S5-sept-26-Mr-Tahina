import org.omg.CORBA.ORB;
import org.omg.CosNaming.*;
import CalculApp.*;

public class Client
{
    public static void main(String [] args) throws DivisionParZero
    {
        try {
            ORB orb=ORB.init(args, null);
            org.omg.CORBA.Object namingContextObject =orb.resolve_initial_references("NameService");
            NamingContextExt namingContext=NamingContextExtHelper.narrow(namingContextObject);
            Calculateur calc=CalculateurHelper.narrow(namingContext.resolve_str("CalculateurService"));
            try {
                // tes appels de méthodes (ajouter, soustraire, division...)
                double resultat=calc.ajouter(12.5, 7.6);
                double resultatSoustraction=calc.soustraire(12.5, 7.5);
                System.out.println("Resulat recu du serveur addition:"+resultat);
                System.out.println("Resulat recu du serveur soustraction:"+resultatSoustraction);
                Operation op = new Operation(15.0, 3.0, "+");
                double[] mesNotes = {14.5, 16.0, 12.5};
                double moyenne = calc.calculerMoyenne(mesNotes);
                System.out.println("Resulat recu du serveur moyenne:"+moyenne);
                double resultatOperation = calc.calculer(op);
                System.out.println("Resulat recu du serveur operation:"+resultatOperation);
                double resultatDivision=calc.division(12, 0);
                System.out.println("Resulat recu du serveur division:"+resultatDivision);
            } 
            catch (DivisionParZero e) {
                //  Bloc spécifique pour intercepter l'erreur CORBA
                System.out.println("Attention : " + e.message); 
            } 
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}