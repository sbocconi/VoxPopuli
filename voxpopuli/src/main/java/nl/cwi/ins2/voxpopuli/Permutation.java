package nl.cwi.ins2.voxpopuli;

/**
 * <p>Title: Vox Populi</p>
 *
 * <p>Description: Automatic Generation of Biased Video Documentaries</p>
 *
 * <p>Copyright: Copyleft: free to copy</p>
 *
 * <p>Company: CWI</p>
 *
 * @Stefano Bocconi
 * @version 1.0
 *  * http://www2.toki.or.id/book/AlgDesignManual/BOOK/BOOK4/NODE151.HTM
 *
 * http://www.idt.mdh.se/kpt/Homepage/source/rank
 * #!/usr/bin/env python

"""
    It is sometimes necessary to generate all permutations of length n.
    For instance, there are six permutations of [1,2,3]:

        [1,2,3]    [1,3,2]    [2,1,3]    [2,3,1]    [3,1,2]    [3,2,1]

    In general there are n! permutations of an n-element list.
    While there are a couple of ways to generate all permutations,
    the following code relies on defining the functions rank/unrank.

    rank(p)                     what is the position of permutation p
                                in the order of all permutations?

    unrank(r, n)                which permutation is in position r of
                                the n! permutations of n! items?

    These two functions must be inverses, e.g., unrank(rank(p), n) == p
    for all permutations p.

    Once rank/unrank are available, you can solve a list of problems:

    Sequencing permutations:
                                To determine the next permutation after p,
                                simply rank p, add 1, and unrank it.
                                It is just as easy to get the permutation
                                right before p.

                                To generate all permutations, simply go
                                through all numbers 0 .. n!-1 and unrank them.

    Generate a random permutation:
                                Select a random integer from 0 to n!-1
                                and unrank it.

    Keep track of permutations:
                                If you want to remember which permutations
                                you have seen, use an n!-bit bit vector
                                and set bit i if unrank(i, n) has been seen.

"""

__author__ = "Kjell Post"
__credits__ = "Steven Skiena, The Algorithm Design Manual"

from __future__ import generators

def fac(n):
    p = 1
    for i in range(1,n+1):
        p *= i
    return p

# rank: permutation -> integer
#
# rank takes a permuted list and returns its rank
#
def rank(p):
    n = len(p)
    r = 0
    for j in range(n):
        r = r + (p[j]-1)*fac(n-j-1)
        for i in range(j+1,n):
            if p[i] > p[j]:
                p[i] -= 1
    return r

# unrank: integer * list size -> permutation
#
# unrank takes a rank number, r, and a number of elements, n,
# and returns the permutation in position r among all the n!
# permutations.
#

def unrank(r, n):
    p = [0]*(n)
    p[n-1] = 1
    for j in range(n):
        d = (r % fac(j+1))/fac(j)
        r = r - d*fac(j)
        p[n-j-1] = d+1
        for i in range(n-j,n):
            if p[i] > d:
                p[i] += 1
    return p

# perm_generator: list size -> generator
#
# perm_generator returns a generator for permutations with n elements
# such that repeated applications of next() returns successive
# permutations of [1, 2, ..., n]
#

def perm_generator(n):
    for p in range(fac(n)):
        yield(unrank(p,n))

if __name__ == "__main__":

    for p in perm_generator(4):     # generate all permutations of [1,2,3,4]
        print p


 * http://www.cs.auckland.ac.nz/compsci720s1c/lectures/objects/Permutations/ranking.cpp
 *
 * // Rank and Unrank functions from "Constructive Combinatorics"
// mjd@cs.auckland.ac.nz
//
#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

typedef vector< int > Perm;

int rank(const Perm &P)
{
  Perm P2;
  int n = P.size();
  if (n==1) return 0;

  int j=0;
  for (int i = 0; i < n; i++)
  {
        if (P[i]!=n) P2.push_back(P[i]);
        else j=i;
  }
  int R = rank(P2);
  if (R % 2) return n*R+j;
  else       return n*R+n-j-1;
}

void unrank(int num, Perm &perm)
{
  int n = perm.size();
  for (int i = 0; i < n; i++) perm[i]=0;

  int P = num, R, k, Dir, C;
  for (int j = n; j > 0; j--)
  {
    R = P % j;
    P = P / j;

    if (P % 2 ) { k = 0; Dir = 1; }
    else        { k = n+1; Dir = -1; }

    C = 0;
    do {
      k = k + Dir;
      if (perm[k-1] == 0) C++;
    }
    while (C != R+1);

    perm[k-1]=j;
  }
  return;
}

int fact(int n)
{
  if (n<2) return 1;
  return n*fact(n-1);
}

int main(int argc, char* argv[])
{
  int n=atoi(argv[1]);
  Perm perm(n); for (int i=0; i<n; i++) perm[i]=i+1;
  int cnt=0;
  Perm perm2(n);

  cout << "Enumeration of permutations (two different orders).\n";
  do {
    for (int j=0; j<n; j++) cout << perm[j];
    cout << " Lexicographic rank=" << cnt;
    cout << " (Johnson/Trotter rank=" << rank(perm) << ")\n";
    unrank(cnt,perm2);
    for (int j=0; j<n; j++) cout << perm2[j];
    cout << " Johnson/Trotter rank=" << cnt++ << endl << endl;
  }
  while ( next_permutation(perm.begin(), perm.end()) );
}

 */



/*********************************** Permutation *****************************/
public class Permutation{

  /*********************
   *  CONSTANTS        *
   *********************/
  final int MAX_ELEMENTS = 20;
  final long ACCEPTABLESTEPS = 10000;

  /*************************
   *  PSEUDO VARIABLES     *
   *  set once and forever *
   *  with error checking  *
   *************************/


  /*********************
   *  VARIABLES *
   **********************/

  private int a[];
  private int maxsize;

  private long Combinations;

  private long GoodRanks[];

  private long CurrentRank;
  private long RankIncrement;

  private Util u; // access to utility functions

  /*********************
   *  CLASSES *
   **********************/


  /*********************
    *  FUNCTIONS *
   **********************/

  // c is the size of the permutation vector
  public Permutation( int c ) throws Exception{
    if( c >= 0 ){
      a = new int[c];
      maxsize = c;
    } else{
      a = new int[MAX_ELEMENTS];
      maxsize = MAX_ELEMENTS;
    }


  }

  public void TestStep( int si, long st ) throws Exception{

    Permutation A = new Permutation( si );
    long limit = Util.fact( si );
    long steps = limit / st;

    System.out.println( "There will be " + steps + " steps" );

    for( long i = 0; i < limit; i = i + st ){
      A.Unrank( i );
      //System.out.println("Rank " + i);
    }
  }

  public boolean TestRank( int s, long r ) throws Exception{

    long limit = Util.fact( s );

    if( limit < r ){
      System.out.println( "Rank out of limit " );
      return false;

    } else{

      Permutation A = new Permutation( s );

      System.out.println( "Fact(" + s + ") is " + limit + " and the result is " +
                          ( ( ( A.Unrank( r ) ).Rank() == r ) ?
                            "OK" : "NOT OK" ) );
    }
    return true;
  }

  public void Init( long steps ) throws Exception{

    Combinations = Util.fact( maxsize );

    for( int i = 0; i < maxsize; i++ ){
      this.Set( i, i );
    }

    if( steps == 0 ){
      RankIncrement = ( long )( Combinations / ACCEPTABLESTEPS );
    }else{
      RankIncrement = ( long )( Combinations / steps );
    }

    System.out.println( "There will be " + steps + " steps" );

    GoodRanks = new long[0];

  }

  public Permutation( Permutation Copy ) throws Exception{
    if( Copy != null ){
      maxsize = Copy.maxsize;
      a = new int[maxsize];
      for( int i = 0; i < maxsize; i++ ){
        a[i] = Copy.a[i];
      }
    } else{
      throw new Exception( "Null in copy constructor " );
    }

  }

  // This function is not optimized (one allocation of memory each new item)
  public boolean AddGoodRank(long Rank){

    int size = GoodRanks.length;

    long[] Temp = new long[size + 1];


    for( int i=0; i<size; i++){
      Temp[i] = GoodRanks[i];
    }

    Temp[size] = Rank;

    GoodRanks = Temp;

    return true;
  }

  public long GetCurrentRank(){
    return CurrentRank;
  }

  public void SetCurrentRank(long a){
    CurrentRank = a;
  }
  public boolean IncrementRank(){
    CurrentRank = CurrentRank + RankIncrement;
    if( CurrentRank < Combinations ){
      return true;
    }
    return false;
  }

  public int Size(){
    return maxsize;
  }

  public int Set( int val, int pos ) throws Exception{

    if( pos < maxsize && pos >= 0 ){
      a[pos] = val;
    } else{
      throw new Exception( "Out of range in permutation " + pos );
    }

    return pos;
  }

  public void Reset( int val ){

    for( int i = 0; i < maxsize; i++ ){
      a[i] = val;
    }
  }

  private int Get( int c ) throws Exception{
    if( ( c >= 0 ) && ( c < maxsize ) ){
    } else{
      throw new Exception( "Get index out of range " );
    }
    return a[c];
  }

  public long Rank() throws Exception{

    int n = this.Size();

    if( n == 1 ){
      return 0;
    }

    Permutation P2 = new Permutation( n - 1 );


    int j = 0, insert = 0;
    for( int i = 0; i < n; i++ ){
      int val = this.Get( i );
      if( val != n ){
        P2.Set( val, insert );
        insert++;
      } else{
        j = i;
      }
    }
    long R = P2.Rank();

    if( ( R % 2 ) != 0 ){
      return n * R + j;
    }

    return( n * R + n - j - 1 );
  }

  public Permutation Unrank( long num ) throws Exception{

    int n = this.Size();

    Permutation Perm = new Permutation( n );

    Perm.Reset( 0 );

    long P = num, R;
    int k, Dir, C;

    for( int j = n; j > 0; j-- ){

      R = P % j;
      P = P / j;

      if( ( P % 2 ) != 0 ){
        k = 0;
        Dir = 1;
      } else{
        k = n + 1;
        Dir = -1;
      }

      C = 0;
      do{
        k = k + Dir;
        if( Perm.Get( k - 1 ) == 0 ){
          C++;
        }
      } while( C != R + 1 );

      Perm.Set( j, k - 1 );
    }
    return Perm;
  }
};
