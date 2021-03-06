/* Generated By:JavaCC: Do not edit this line. QlsTokenManager.java */
package qls;
import java.io.FileInputStream;
import java.io.File;
import qls.syntaxtree.*;
import qls.visitor.*;
import qls.interpreter.*;
import qls.core.*;
import qls.syntaxtree.*;

/** Token Manager. */
public class QlsTokenManager implements QlsConstants
{

  /** Debug output. */
  public static  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public static  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private static final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0x40000000000L) != 0L)
            return 19;
         if ((active0 & 0x1fffff80L) != 0L)
         {
            jjmatchedKind = 50;
            return 13;
         }
         return -1;
      case 1:
         if ((active0 & 0x210900L) != 0L)
            return 13;
         if ((active0 & 0x1fdef680L) != 0L)
         {
            jjmatchedKind = 50;
            jjmatchedPos = 1;
            return 13;
         }
         return -1;
      case 2:
         if ((active0 & 0x3029080L) != 0L)
            return 13;
         if ((active0 & 0x1cdc6600L) != 0L)
         {
            jjmatchedKind = 50;
            jjmatchedPos = 2;
            return 13;
         }
         return -1;
      case 3:
         if ((active0 & 0x1cd46400L) != 0L)
         {
            jjmatchedKind = 50;
            jjmatchedPos = 3;
            return 13;
         }
         if ((active0 & 0x80200L) != 0L)
            return 13;
         return -1;
      case 4:
         if ((active0 & 0x1cd44000L) != 0L)
         {
            jjmatchedKind = 50;
            jjmatchedPos = 4;
            return 13;
         }
         if ((active0 & 0x2400L) != 0L)
            return 13;
         return -1;
      case 5:
         if ((active0 & 0x4904000L) != 0L)
            return 13;
         if ((active0 & 0x18440000L) != 0L)
         {
            jjmatchedKind = 50;
            jjmatchedPos = 5;
            return 13;
         }
         return -1;
      case 6:
         if ((active0 & 0x18000000L) != 0L)
         {
            jjmatchedKind = 50;
            jjmatchedPos = 6;
            return 13;
         }
         if ((active0 & 0x440000L) != 0L)
            return 13;
         return -1;
      default :
         return -1;
   }
}
private static final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
static private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
static private int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 33:
         return jjMoveStringLiteralDfa1_0(0x100000000000000L);
      case 37:
         return jjStopAtPos(0, 43);
      case 38:
         return jjStopAtPos(0, 44);
      case 40:
         return jjStopAtPos(0, 53);
      case 41:
         return jjStopAtPos(0, 54);
      case 42:
         return jjStopAtPos(0, 41);
      case 43:
         return jjStopAtPos(0, 39);
      case 44:
         return jjStopAtPos(0, 31);
      case 45:
         jjmatchedKind = 40;
         return jjMoveStringLiteralDfa1_0(0x100000000L);
      case 46:
         return jjStopAtPos(0, 29);
      case 47:
         return jjStartNfaWithStates_0(0, 42, 19);
      case 58:
         return jjStopAtPos(0, 30);
      case 60:
         jjmatchedKind = 35;
         return jjMoveStringLiteralDfa1_0(0x6000000000L);
      case 61:
         jjmatchedKind = 33;
         return jjMoveStringLiteralDfa1_0(0x80000000000000L);
      case 62:
         jjmatchedKind = 34;
         return jjMoveStringLiteralDfa1_0(0x1000000000L);
      case 99:
         return jjMoveStringLiteralDfa1_0(0x1004000L);
      case 100:
         return jjMoveStringLiteralDfa1_0(0x800L);
      case 101:
         return jjMoveStringLiteralDfa1_0(0x1000L);
      case 102:
         return jjMoveStringLiteralDfa1_0(0x8000000L);
      case 103:
         return jjMoveStringLiteralDfa1_0(0x500000L);
      case 105:
         return jjMoveStringLiteralDfa1_0(0x2000100L);
      case 108:
         return jjMoveStringLiteralDfa1_0(0x2000L);
      case 109:
         return jjMoveStringLiteralDfa1_0(0x280000L);
      case 110:
         return jjMoveStringLiteralDfa1_0(0x8000L);
      case 112:
         return jjMoveStringLiteralDfa1_0(0x40000L);
      case 114:
         return jjMoveStringLiteralDfa1_0(0x800000L);
      case 115:
         return jjMoveStringLiteralDfa1_0(0x4020000L);
      case 116:
         return jjMoveStringLiteralDfa1_0(0x10200L);
      case 117:
         return jjMoveStringLiteralDfa1_0(0x80L);
      case 118:
         return jjMoveStringLiteralDfa1_0(0x10000000L);
      case 119:
         return jjMoveStringLiteralDfa1_0(0x400L);
      default :
         return jjMoveNfa_0(0, 0);
   }
}
static private int jjMoveStringLiteralDfa1_0(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 61:
         if ((active0 & 0x1000000000L) != 0L)
            return jjStopAtPos(1, 36);
         else if ((active0 & 0x2000000000L) != 0L)
            return jjStopAtPos(1, 37);
         else if ((active0 & 0x80000000000000L) != 0L)
            return jjStopAtPos(1, 55);
         else if ((active0 & 0x100000000000000L) != 0L)
            return jjStopAtPos(1, 56);
         break;
      case 62:
         if ((active0 & 0x100000000L) != 0L)
            return jjStopAtPos(1, 32);
         else if ((active0 & 0x4000000000L) != 0L)
            return jjStopAtPos(1, 38);
         break;
      case 97:
         return jjMoveStringLiteralDfa2_0(active0, 0x10080000L);
      case 101:
         if ((active0 & 0x200000L) != 0L)
            return jjStartNfaWithStates_0(1, 21, 13);
         return jjMoveStringLiteralDfa2_0(active0, 0xc28000L);
      case 102:
         if ((active0 & 0x100L) != 0L)
            return jjStartNfaWithStates_0(1, 8, 13);
         break;
      case 104:
         return jjMoveStringLiteralDfa2_0(active0, 0x600L);
      case 108:
         return jjMoveStringLiteralDfa2_0(active0, 0x100000L);
      case 109:
         return jjMoveStringLiteralDfa2_0(active0, 0x1000000L);
      case 110:
         return jjMoveStringLiteralDfa2_0(active0, 0x2001000L);
      case 111:
         if ((active0 & 0x800L) != 0L)
            return jjStartNfaWithStates_0(1, 11, 13);
         else if ((active0 & 0x10000L) != 0L)
            return jjStartNfaWithStates_0(1, 16, 13);
         return jjMoveStringLiteralDfa2_0(active0, 0x2000L);
      case 114:
         return jjMoveStringLiteralDfa2_0(active0, 0x44000L);
      case 115:
         return jjMoveStringLiteralDfa2_0(active0, 0x80L);
      case 116:
         return jjMoveStringLiteralDfa2_0(active0, 0x4000000L);
      case 117:
         return jjMoveStringLiteralDfa2_0(active0, 0x8000000L);
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
static private int jjMoveStringLiteralDfa2_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 99:
         return jjMoveStringLiteralDfa3_0(active0, 0x2000L);
      case 100:
         if ((active0 & 0x1000L) != 0L)
            return jjStartNfaWithStates_0(2, 12, 13);
         else if ((active0 & 0x1000000L) != 0L)
            return jjStartNfaWithStates_0(2, 24, 13);
         break;
      case 101:
         if ((active0 & 0x80L) != 0L)
            return jjStartNfaWithStates_0(2, 7, 13);
         return jjMoveStringLiteralDfa3_0(active0, 0x4200L);
      case 105:
         return jjMoveStringLiteralDfa3_0(active0, 0x80400L);
      case 110:
         return jjMoveStringLiteralDfa3_0(active0, 0x8000000L);
      case 111:
         return jjMoveStringLiteralDfa3_0(active0, 0x140000L);
      case 114:
         return jjMoveStringLiteralDfa3_0(active0, 0x14000000L);
      case 116:
         if ((active0 & 0x20000L) != 0L)
            return jjStartNfaWithStates_0(2, 17, 13);
         else if ((active0 & 0x2000000L) != 0L)
            return jjStartNfaWithStates_0(2, 25, 13);
         return jjMoveStringLiteralDfa3_0(active0, 0xc00000L);
      case 119:
         if ((active0 & 0x8000L) != 0L)
            return jjStartNfaWithStates_0(2, 15, 13);
         break;
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
static private int jjMoveStringLiteralDfa3_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa4_0(active0, 0x10006000L);
      case 98:
         return jjMoveStringLiteralDfa4_0(active0, 0x100000L);
      case 99:
         return jjMoveStringLiteralDfa4_0(active0, 0x8040000L);
      case 102:
         return jjMoveStringLiteralDfa4_0(active0, 0x400000L);
      case 105:
         return jjMoveStringLiteralDfa4_0(active0, 0x4000000L);
      case 108:
         return jjMoveStringLiteralDfa4_0(active0, 0x400L);
      case 110:
         if ((active0 & 0x200L) != 0L)
            return jjStartNfaWithStates_0(3, 9, 13);
         else if ((active0 & 0x80000L) != 0L)
            return jjStartNfaWithStates_0(3, 19, 13);
         break;
      case 117:
         return jjMoveStringLiteralDfa4_0(active0, 0x800000L);
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
static private int jjMoveStringLiteralDfa4_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa5_0(active0, 0x100000L);
      case 101:
         if ((active0 & 0x400L) != 0L)
            return jjStartNfaWithStates_0(4, 10, 13);
         return jjMoveStringLiteralDfa5_0(active0, 0x40000L);
      case 108:
         if ((active0 & 0x2000L) != 0L)
            return jjStartNfaWithStates_0(4, 13, 13);
         break;
      case 110:
         return jjMoveStringLiteralDfa5_0(active0, 0x4000000L);
      case 114:
         return jjMoveStringLiteralDfa5_0(active0, 0x10c00000L);
      case 116:
         return jjMoveStringLiteralDfa5_0(active0, 0x8004000L);
      default :
         break;
   }
   return jjStartNfa_0(3, active0);
}
static private int jjMoveStringLiteralDfa5_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(3, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(4, active0);
      return 5;
   }
   switch(curChar)
   {
      case 101:
         if ((active0 & 0x4000L) != 0L)
            return jjStartNfaWithStates_0(5, 14, 13);
         return jjMoveStringLiteralDfa6_0(active0, 0x40000L);
      case 103:
         if ((active0 & 0x4000000L) != 0L)
            return jjStartNfaWithStates_0(5, 26, 13);
         break;
      case 105:
         return jjMoveStringLiteralDfa6_0(active0, 0x8000000L);
      case 108:
         if ((active0 & 0x100000L) != 0L)
            return jjStartNfaWithStates_0(5, 20, 13);
         break;
      case 110:
         if ((active0 & 0x800000L) != 0L)
            return jjStartNfaWithStates_0(5, 23, 13);
         break;
      case 111:
         return jjMoveStringLiteralDfa6_0(active0, 0x400000L);
      case 114:
         return jjMoveStringLiteralDfa6_0(active0, 0x10000000L);
      default :
         break;
   }
   return jjStartNfa_0(4, active0);
}
static private int jjMoveStringLiteralDfa6_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(4, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(5, active0);
      return 6;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa7_0(active0, 0x10000000L);
      case 100:
         if ((active0 & 0x40000L) != 0L)
            return jjStartNfaWithStates_0(6, 18, 13);
         break;
      case 109:
         if ((active0 & 0x400000L) != 0L)
            return jjStartNfaWithStates_0(6, 22, 13);
         break;
      case 111:
         return jjMoveStringLiteralDfa7_0(active0, 0x8000000L);
      default :
         break;
   }
   return jjStartNfa_0(5, active0);
}
static private int jjMoveStringLiteralDfa7_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(5, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(6, active0);
      return 7;
   }
   switch(curChar)
   {
      case 110:
         if ((active0 & 0x8000000L) != 0L)
            return jjStartNfaWithStates_0(7, 27, 13);
         break;
      case 121:
         if ((active0 & 0x10000000L) != 0L)
            return jjStartNfaWithStates_0(7, 28, 13);
         break;
      default :
         break;
   }
   return jjStartNfa_0(6, active0);
}
static private int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
static final long[] jjbitVec0 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
static private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 30;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 19:
                  if (curChar == 42)
                     jjCheckNAddTwoStates(25, 26);
                  else if (curChar == 47)
                     jjCheckNAddStates(0, 2);
                  break;
               case 0:
                  if ((0x3fe000000000000L & l) != 0L)
                  {
                     if (kind > 45)
                        kind = 45;
                     jjCheckNAddTwoStates(1, 2);
                  }
                  else if (curChar == 47)
                     jjAddStates(3, 4);
                  else if (curChar == 48)
                  {
                     if (kind > 45)
                        kind = 45;
                     jjCheckNAddStates(5, 7);
                  }
                  else if (curChar == 39)
                     jjCheckNAddStates(8, 10);
                  break;
               case 1:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 45)
                     kind = 45;
                  jjCheckNAddTwoStates(1, 2);
                  break;
               case 3:
                  if (curChar == 39)
                     jjCheckNAddStates(8, 10);
                  break;
               case 4:
                  if ((0xffffff7fffffffffL & l) != 0L)
                     jjCheckNAddStates(8, 10);
                  break;
               case 6:
                  if ((0x8400000000L & l) != 0L)
                     jjCheckNAddStates(8, 10);
                  break;
               case 7:
                  if (curChar == 39 && kind > 49)
                     kind = 49;
                  break;
               case 8:
                  if ((0xff000000000000L & l) != 0L)
                     jjCheckNAddStates(11, 14);
                  break;
               case 9:
                  if ((0xff000000000000L & l) != 0L)
                     jjCheckNAddStates(8, 10);
                  break;
               case 10:
                  if ((0xf000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 11;
                  break;
               case 11:
                  if ((0xff000000000000L & l) != 0L)
                     jjCheckNAdd(9);
                  break;
               case 13:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 50)
                     kind = 50;
                  jjstateSet[jjnewStateCnt++] = 13;
                  break;
               case 14:
                  if (curChar != 48)
                     break;
                  if (kind > 45)
                     kind = 45;
                  jjCheckNAddStates(5, 7);
                  break;
               case 16:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 45)
                     kind = 45;
                  jjCheckNAddTwoStates(16, 2);
                  break;
               case 17:
                  if ((0xff000000000000L & l) == 0L)
                     break;
                  if (kind > 45)
                     kind = 45;
                  jjCheckNAddTwoStates(17, 2);
                  break;
               case 18:
                  if (curChar == 47)
                     jjAddStates(3, 4);
                  break;
               case 20:
                  if ((0xffffffffffffdbffL & l) != 0L)
                     jjCheckNAddStates(0, 2);
                  break;
               case 21:
                  if ((0x2400L & l) != 0L && kind > 5)
                     kind = 5;
                  break;
               case 22:
                  if (curChar == 10 && kind > 5)
                     kind = 5;
                  break;
               case 23:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 22;
                  break;
               case 24:
                  if (curChar == 42)
                     jjCheckNAddTwoStates(25, 26);
                  break;
               case 25:
                  if ((0xfffffbffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(25, 26);
                  break;
               case 26:
                  if (curChar == 42)
                     jjAddStates(15, 16);
                  break;
               case 27:
                  if ((0xffff7fffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(28, 26);
                  break;
               case 28:
                  if ((0xfffffbffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(28, 26);
                  break;
               case 29:
                  if (curChar == 47 && kind > 6)
                     kind = 6;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
               case 13:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 50)
                     kind = 50;
                  jjCheckNAdd(13);
                  break;
               case 2:
                  if ((0x100000001000L & l) != 0L && kind > 45)
                     kind = 45;
                  break;
               case 4:
                  if ((0xffffffffefffffffL & l) != 0L)
                     jjCheckNAddStates(8, 10);
                  break;
               case 5:
                  if (curChar == 92)
                     jjAddStates(17, 19);
                  break;
               case 6:
                  if ((0x14404410000000L & l) != 0L)
                     jjCheckNAddStates(8, 10);
                  break;
               case 15:
                  if ((0x100000001000000L & l) != 0L)
                     jjCheckNAdd(16);
                  break;
               case 16:
                  if ((0x7e0000007eL & l) == 0L)
                     break;
                  if (kind > 45)
                     kind = 45;
                  jjCheckNAddTwoStates(16, 2);
                  break;
               case 20:
                  jjAddStates(0, 2);
                  break;
               case 25:
                  jjCheckNAddTwoStates(25, 26);
                  break;
               case 27:
               case 28:
                  jjCheckNAddTwoStates(28, 26);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 4:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjAddStates(8, 10);
                  break;
               case 20:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjAddStates(0, 2);
                  break;
               case 25:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjCheckNAddTwoStates(25, 26);
                  break;
               case 27:
               case 28:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjCheckNAddTwoStates(28, 26);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 30 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   20, 21, 23, 19, 24, 15, 17, 2, 4, 5, 7, 4, 5, 9, 7, 27, 
   29, 6, 8, 10, 
};

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, null, null, "\165\163\145", "\151\146", 
"\164\150\145\156", "\167\150\151\154\145", "\144\157", "\145\156\144", "\154\157\143\141\154", 
"\143\162\145\141\164\145", "\156\145\167", "\164\157", "\163\145\164", "\160\162\157\143\145\145\144", 
"\155\141\151\156", "\147\154\157\142\141\154", "\155\145", "\147\145\164\146\162\157\155", 
"\162\145\164\165\162\156", "\143\155\144", "\151\156\164", "\163\164\162\151\156\147", 
"\146\165\156\143\164\151\157\156", "\166\141\162\141\162\162\141\171", "\56", "\72", "\54", "\55\76", "\75", 
"\76", "\74", "\76\75", "\74\75", "\74\76", "\53", "\55", "\52", "\57", "\45", "\46", 
null, null, null, null, null, null, null, null, "\50", "\51", "\75\75", "\41\75", };

/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT",
};
static final long[] jjtoToken = {
   0x1e63fffffffff81L, 
};
static final long[] jjtoSkip = {
   0x7eL, 
};
static protected SimpleCharStream input_stream;
static private final int[] jjrounds = new int[30];
static private final int[] jjstateSet = new int[60];
static protected char curChar;
/** Constructor. */
public QlsTokenManager(SimpleCharStream stream){
   if (input_stream != null)
      throw new TokenMgrError("ERROR: Second call to constructor of static lexer. You must use ReInit() to initialize the static variables.", TokenMgrError.STATIC_LEXER_ERROR);
   input_stream = stream;
}

/** Constructor. */
public QlsTokenManager(SimpleCharStream stream, int lexState){
   this(stream);
   SwitchTo(lexState);
}

/** Reinitialise parser. */
static public void ReInit(SimpleCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
static private void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 30; i-- > 0;)
      jjrounds[i] = 0x80000000;
}

/** Reinitialise parser. */
static public void ReInit(SimpleCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}

/** Switch to specified lex state. */
static public void SwitchTo(int lexState)
{
   if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

static protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   curTokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind, curTokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}

static int curLexState = 0;
static int defaultLexState = 0;
static int jjnewStateCnt;
static int jjround;
static int jjmatchedPos;
static int jjmatchedKind;

/** Get the next Token. */
public static Token getNextToken() 
{
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(java.io.IOException e)
   {
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   try { input_stream.backup(0);
      while (curChar <= 32 && (0x100002600L & (1L << curChar)) != 0L)
         curChar = input_stream.BeginToken();
   }
   catch (java.io.IOException e1) { continue EOFLoop; }
   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         return matchedToken;
      }
      else
      {
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

static private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
static private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
static private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

static private void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}

}
