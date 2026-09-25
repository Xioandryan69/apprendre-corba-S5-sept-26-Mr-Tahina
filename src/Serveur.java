import CalculApp.Calculateur;
import CalculApp.CalculateurHelper;
import CalculApp.CalculateurImpl;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
public class Serveur {
    public static void main(String [ ] args)
    {
        try 
        {
            ORB orb= ORB.init(args,null);
            POA rootPOA= POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            CalculateurImpl calculateurImpl=new CalculateurImpl();
            org.omg.CORBA.Object ref=rootPOA.servant_to_reference(calculateurImpl);
            Calculateur calculateur=CalculateurHelper.narrow(ref);
            String ior=orb.object_to_string(calculateur);
            System.out.println("IOR=");
            System.out.println(ior);

            rootPOA.the_POAManager().activate();
            System.out.println("Serveur CORBA demarre ..");
            orb.run();
            
        }catch(Exception e){
             e.printStackTrace();

        }
    }

    
}
