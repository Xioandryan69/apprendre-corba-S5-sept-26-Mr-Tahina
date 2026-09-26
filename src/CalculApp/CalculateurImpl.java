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
    
}
