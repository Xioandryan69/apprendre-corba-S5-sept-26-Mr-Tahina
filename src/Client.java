import org.omg.CORBA.ORB;
import org.omg.CosNaming.*;
import CalculApp.*;

public class Client
{
    public static void main(String [] args)
    {
        try {
            ORB orb=ORB.init(args, null);
            org.omg.CORBA.Object namingContextObject =orb.resolve_initial_references("NameService");
            NamingContextExt namingContext=NamingContextExtHelper.narrow(namingContextObject);
            Calculateur calc=CalculateurHelper.narrow(namingContext.resolve_str("CalculateurService"));
            double resultat=calc.ajouter(12.5, 7.5);
            System.out.println("Resulat recu du serveur :"+resultat);

            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}