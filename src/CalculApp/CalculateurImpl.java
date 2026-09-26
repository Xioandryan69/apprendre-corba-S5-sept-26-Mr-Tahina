package CalculApp;

public class CalculateurImpl extends  CalculateurPOA
{
    @Override 
    public double ajouter(double a,double b)
    {
        return a+b;
    }

    @Override 
    public double soustraire(double a,double b)
    {
        return a-b;
    }
    @Override  
    public double division(double a ,double b) throws DivisionParZero{

        if(b==0)
        {
            throw new DivisionParZero("une divison ne doit pas etre diviser par zero");
        }else{
            return  a/b;
        }
    }
    @Override
    public double calculer(Operation op) throws DivisionParZero {
        if (op.typeOperation.equals("+")) {
            return op.nombreA + op.nombreB;
        } else if (op.typeOperation.equals("-")) {
            return op.nombreA - op.nombreB;
        } else if (op.typeOperation.equals("/")) {
            if (op.nombreB == 0) {
                throw new DivisionParZero("Division par zéro impossible !");
            }
            return op.nombreA / op.nombreB;
        }
        return 0;
    }
    @Override 
    public double calculerMoyenne( double[] nombres)
    {
        double somme =0;
        for(double nombre : nombres)
            {
                somme+=nombre;
            }
        return somme/nombres.length ;
    }
        
}
