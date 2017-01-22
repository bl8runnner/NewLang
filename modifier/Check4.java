package modifier;

import exception.ParserException;

public enum Check4
{
    METHODS(new CombinationChecker()
        {
            //
            // pr�fe 'abstract' bei Methoden
            //
            private final Modifiers.ModifierType[] INVALID4ABSTRACT = new Modifiers.ModifierType[]
                                                                        {
                            Modifiers.ModifierType.STATIC, Modifiers.ModifierType.FINAL,
                            Modifiers.ModifierType.PRIVATE             };
            
            public void check(final Abstract modifier) throws Exception
            {
                this.check(Modifiers.ModifierType.ABSTRACT, INVALID4ABSTRACT);
            }
            
            //
            // pr�fe 'const' bei Methoden
            //
            public void check(final Const modifier) throws ParserException
            {
            }
            
            //
            // pr�fe 'final' bei Methoden
            //
            private final Modifiers.ModifierType[] INVALID4FINAL = new Modifiers.ModifierType[]
                                                                     {
                            Modifiers.ModifierType.STATIC, Modifiers.ModifierType.ABSTRACT,
                            Modifiers.ModifierType.PRIVATE          };
            
            public void check(final Final modifier) throws Exception
            {
                this.check(Modifiers.ModifierType.FINAL, INVALID4FINAL);
            }
            
            //
            // pr�fe 'private' bei Methoden
            //
            private final Modifiers.ModifierType[] INVALID4PRIVATE = new Modifiers.ModifierType[]
                                                                       {
                            Modifiers.ModifierType.FINAL, Modifiers.ModifierType.ABSTRACT,
                            Modifiers.ModifierType.PROTECTED, Modifiers.ModifierType.PUBLIC };
            
            public void check(final Private modifier) throws Exception
            {
                this.check(Modifiers.ModifierType.PRIVATE, INVALID4PRIVATE);
            }
            
            //
            // pr�fe 'protected' bei Methoden
            //
            private final Modifiers.ModifierType[] INVALID4PROTECTED = new Modifiers.ModifierType[]
                                                                         {
                            Modifiers.ModifierType.PRIVATE, Modifiers.ModifierType.PUBLIC };
            
            public void check(final Protected modifier) throws Exception
            {
                this.check(Modifiers.ModifierType.PROTECTED, INVALID4PROTECTED);
            }
            
            //
            // pr�fe 'public' bei Methoden
            //
            private final Modifiers.ModifierType[] INVALID4PUBLIC = new Modifiers.ModifierType[]
                                                                      {
                            Modifiers.ModifierType.PROTECTED, Modifiers.ModifierType.PUBLIC };
            
            public void check(final Public modifier) throws Exception
            {
                this.check(Modifiers.ModifierType.PUBLIC, INVALID4PUBLIC);
            }
            
            //
            // pr�fe 'static' bei Methoden
            //
            private final Modifiers.ModifierType[] INVALID4STATIC = new Modifiers.ModifierType[]
                                                                      {
                            Modifiers.ModifierType.ABSTRACT, Modifiers.ModifierType.FINAL };
            
            public void check(final Static modifier) throws Exception
            {
                this.check(Modifiers.ModifierType.STATIC, INVALID4STATIC);
            }
            
        }
    
    ), CLASSES(new CombinationChecker()
        {
            //
            // pr�fe 'abstract' bei Klassen
            //
            private final Modifiers.ModifierType[] INVALID4ABSTRACT = new Modifiers.ModifierType[]
                                                                        { Modifiers.ModifierType.FINAL };
            
            public void check(final Abstract modifier) throws Exception
            {
                this.check(Modifiers.ModifierType.ABSTRACT, INVALID4ABSTRACT);
            }
            
            //
            // pr�fe 'final' bei Klassen
            //
            private final Modifiers.ModifierType[] INVALID4FINAL = new Modifiers.ModifierType[]
                                                                     { Modifiers.ModifierType.ABSTRACT };
            
            public void check(final Final modifier) throws Exception
            {
                this.check(Modifiers.ModifierType.FINAL, INVALID4FINAL);
            }
            
            //
            // pr�fe 'private' bei Klassen
            //
            private final Modifiers.ModifierType[] INVALID4PRIVATE = new Modifiers.ModifierType[]
                                                                       {
                            Modifiers.ModifierType.PROTECTED, Modifiers.ModifierType.PUBLIC };
            
            public void check(final Private modifier) throws Exception
            {
                this.check(Modifiers.ModifierType.PRIVATE, INVALID4PRIVATE);
            }
            
            //
            // pr�fe 'protected' bei Klassen
            //
            private final Modifiers.ModifierType[] INVALID4PROTECTED = new Modifiers.ModifierType[]
                                                                         {
                            Modifiers.ModifierType.PRIVATE, Modifiers.ModifierType.PUBLIC };
            
            public void check(final Protected modifier) throws Exception
            {
                this.check(Modifiers.ModifierType.PROTECTED, INVALID4PROTECTED);
            }
            
            //
            // pr�fe 'public' bei Klassen
            //
            private final Modifiers.ModifierType[] INVALID4PUBLIC = new Modifiers.ModifierType[]
                                                                      {
                            Modifiers.ModifierType.PROTECTED, Modifiers.ModifierType.PRIVATE };
            
            public void check(final Public modifier) throws Exception
            {
                this.check(Modifiers.ModifierType.PUBLIC, INVALID4PUBLIC);
            }
            
        }),
    ATTRS(new CombinationChecker()
        {
            //
            // pr�fe 'const' bei Attributen
            //
            public void check(final Const modifier) throws ParserException
            {
            }
            
            //
            // pr�fe 'private' bei Attributen
            //
            private final Modifiers.ModifierType[] INVALID4PRIVATE = new Modifiers.ModifierType[]
                                                                       {
                            Modifiers.ModifierType.PROTECTED, Modifiers.ModifierType.PUBLIC };
            
            public void check(final Private modifier) throws Exception
            {
                this.check(Modifiers.ModifierType.PRIVATE, INVALID4PRIVATE);
            }
            
            //
            // pr�fe 'protected' bei Attributen
            //
            private final Modifiers.ModifierType[] INVALID4PROTECTED = new Modifiers.ModifierType[]
                                                                         {
                            Modifiers.ModifierType.PRIVATE, Modifiers.ModifierType.PUBLIC };
            
            public void check(final Protected modifier) throws Exception
            {
                this.check(Modifiers.ModifierType.PROTECTED, INVALID4PROTECTED);
            }
            
            //
            // pr�fe 'public' bei Attributen
            //
            private final Modifiers.ModifierType[] INVALID4PUBLIC = new Modifiers.ModifierType[]
                                                                      {
                            Modifiers.ModifierType.PROTECTED, Modifiers.ModifierType.PRIVATE };
            
            public void check(final Public modifier) throws Exception
            {
                this.check(Modifiers.ModifierType.PUBLIC, INVALID4PUBLIC);
            }
            
            //
            // pr�fe 'static' bei Attributen
            //
            private final Modifiers.ModifierType[] INVALID4STATIC = new Modifiers.ModifierType[]
                                                                      { Modifiers.ModifierType.TRANSIENT };
            
            public void check(final Static modifier) throws Exception
            {
                this.check(Modifiers.ModifierType.STATIC, INVALID4STATIC);
            }
            
            //
            // pr�fe 'transient' bei Attributen
            //
            private final Modifiers.ModifierType[] INVALID4TRANSIENT = new Modifiers.ModifierType[]
                                                                         { Modifiers.ModifierType.STATIC };
            
            public void check(final Transient modifier) throws Exception
            {
                this.check(Modifiers.ModifierType.TRANSIENT, INVALID4TRANSIENT);
            }
            
        }),
    VARS(new CombinationChecker()
        {
            //
            // pr�fe 'const' bei Variablen
            //
            public void check(final Const modifier) throws ParserException
            {
            }
            
        }),
    PARAMS(new CombinationChecker()
        {
            //
            // pr�fe 'const' bei Parametern
            //
            public void check(final Const modifier) throws ParserException
            {
            }
            
        }
    
    );
    
    private CombinationChecker myCombinationChecker = null;
    
    Check4(final CombinationChecker combinationChecker)
    {
        this.myCombinationChecker = combinationChecker;
    }
    
    public void check(final Abstract modifier) throws Exception
    {
        this.myCombinationChecker.check(modifier);
    }
    
    public void check(final Const modifier) throws ParserException
    {
        this.myCombinationChecker.check(modifier);
    }
    
    public void check(final Final modifier) throws Exception
    {
        this.myCombinationChecker.check(modifier);
    }
    
    public void check(final Private modifier) throws Exception
    {
        this.myCombinationChecker.check(modifier);
    }
    
    public void check(final Protected modifier) throws Exception
    {
        this.myCombinationChecker.check(modifier);
    }
    
    public void check(final Public modifier) throws Exception
    {
        this.myCombinationChecker.check(modifier);
    }
    
    public void check(final Static modifier) throws Exception
    {
        this.myCombinationChecker.check(modifier);
    }
    
    public void check(final Transient modifier) throws Exception
    {
        this.myCombinationChecker.check(modifier);
    }
    
}
