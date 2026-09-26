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
            rootPOA.the_POAManager().activate();
            CalculateurImpl calculateurImpl=new CalculateurImpl();
            org.omg.CORBA.Object ref=rootPOA.servant_to_reference(calculateurImpl);

            org.omg.CORBA.Object namingContextObj=orb.resolve_initial_references("NameService");
            org.omg.CosNaming.NamingContextExt namingContext=org.omg.CosNaming.NamingContextExtHelper.narrow(namingContextObj);
            org.omg.CosNaming.NameComponent  path[]=namingContext.to_name("CalculateurService");
            namingContext.rebind(path, ref);
            

            System.out.println("Serveur CORBA demarre ..");
            orb.run();
            
        }catch(Exception e){
             e.printStackTrace();

        }
    }

    
}
