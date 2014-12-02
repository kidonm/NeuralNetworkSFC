# SFC projekt 2014/2015 #
## Marek Kidon xkidon00 ##
### Uvod ###
Projekt je navrhem a implementaci neuronove site, ktera klasifikuje rukou psane cislice. Jako trenovaci a testovaci sada byly pouzite vzorky z MNIST databaze, ktera je podmnouzinou vetsi databaze NIST. Data se skladaji z 60000 trenovacich dat a 10000 testovacich dat. Databaze je dostupna zdarma online a je nutne si ji stahnout pro spusteni projektu. [MNIST](http://yann.lecun.com/exdb/mnist/)    
Dosazene vysledky je mozne porovnat s jinymi implementacemi, ktere na stejnem miste publikovane.  

### Pouzita Metoda ###


### Dosazene vysledky ###

### Manual ###
#### Pozadavky ####
Pro beh aplikace je nutne mit nainstalovany Java Runtime Environment ve verzi alespon 1.7

#### Spusteni aplikace ####
Projekt je mozne spustit nasledujicim prikazem : 

```
java -cp target/NeuralNetwork-1.0-SNAPSHOT.jar \
 fit.NNSFC.xkidon00.NeuralNetwork \
 data/train-images-idx3-ubyte \
 data/train-labels-idx1-ubyte \
 data/t10k-images-idx3-ubyte \
 data/t10k-labels-idx1-ubyte
```  
kde jednotlive parametry:    

|             Parametr                 |       Vyznam       |
|--------------------------------------|--------------------|
|```target/NeuralNetwork-1.0-SNAPSHOT.jar``` | archiv s projektem |
|```fit.NNSFC.xkidon00.NeuralNetwork```| trida ktera se ma spustit |
|```data/train-images-idx3-ubyte``` | trenovaci data |
|```data/train-labels-idx1-ubyte``` | ohodnoceni trenovacich dat |
|```data/t10k-images-idx3-ubyte``` | testovaci data |
|```data/t10k-labels-idx1-ubyte```| ohodnoceni testovacich dat|

### Ukazka aplikace ###
```
after batch : 0 out of : 60000 -> correctly classified : 1069 out of : 10000
after batch : 60 out of : 60000 -> correctly classified : 1005 out of : 10000
after batch : 120 out of : 60000 -> correctly classified : 1025 out of : 10000
after batch : 180 out of : 60000 -> correctly classified : 1162 out of : 10000
after batch : 240 out of : 60000 -> correctly classified : 1563 out of : 10000
after batch : 300 out of : 60000 -> correctly classified : 1735 out of : 10000
.
.
.
after batch : 4380 out of : 60000 -> correctly classified : 2740 out of : 10000
after batch : 4440 out of : 60000 -> correctly classified : 2779 out of : 10000
after batch : 4500 out of : 60000 -> correctly classified : 2806 out of : 10000
after batch : 4560 out of : 60000 -> correctly classified : 2814 out of : 10000
after batch : 4620 out of : 60000 -> correctly classified : 2835 out of : 10000
after batch : 4680 out of : 60000 -> correctly classified : 2833 out of : 10000

```



