// Title:       RPNHelper
// @version:    $Id: RPNHelper.java,v 1.1.1.1 2008/02/22 05:34:43 verus Exp $
// Copyright:   Copyright (C) 1999,2000 Knowledge Integration Ltd (See the COPYING file for details.)
// @author:     Ian Ibbotson ( ibbo@k-int.com )
// Company:     Knowledge Integration Ltd.
// Description: Utility for dealing with RPN queries... Takes a query_tree and converts
//              it into z3950 RPN query

//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public License
// as published by the Free Software Foundation; either version 2.1 of
// the license, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite
// 330, Boston, MA  02111-1307, USA.
//                                                                                                                                            

package com.k_int.z3950.util;

import com.k_int.codec.runtime.*;
import com.k_int.codec.util.*;
import com.k_int.gen.Z39_50_APDU_1995.*;
import com.k_int.util.RPNQueryRep.*;
import java.util.Vector;
import java.util.Enumeration;
import java.math.BigInteger;


public class RPNHelper
{
    // Take an RPNQueryRep root node and convert it into a Z3950 RPNQuery codec
    public static final RPNQuery_type RootNodeToZRPNStructure(RootNode query_tree) throws InvalidQueryException
    {
        // System.err.println("\n\nRPNHelper::RootNodeToZRPNStructure");
        OIDRegister reg = OIDRegister.getRegister();
        RPNQuery_type result = new RPNQuery_type();

        String qry_attrset = query_tree.getAttrset();

        if ( null != qry_attrset )
        {
            // System.err.println("Setting attributeSet OID to "+qry_attrset);
            result.attributeSet = reg.oidByName(qry_attrset);
        }
        else
        {
            throw new InvalidQueryException("Query does not to have a valid default attrset");
        }

        result.rpn = visitNode(query_tree, reg);

        // System.err.println("Returning result, rpn = " + result.rpn);
        return result;
    }

    public static final RPNStructure_type visitNode(QueryNode node, OIDRegister reg)
    {
        RPNStructure_type retval = null;

        if ( node instanceof RootNode )
        {
            retval = visitNode(((RootNode)node).getChild(), reg);
        }
        else if ( node instanceof ComplexNode )
        {
            retval = new RPNStructure_type();
            retval.which = RPNStructure_type.rpnrpnop_CID;

            rpnRpnOp_inline3_type t =  new rpnRpnOp_inline3_type();
            retval.o = t;
            
            t.rpn1 = visitNode ( ((ComplexNode)node).getLHS(), reg );
            t.rpn2 = visitNode ( ((ComplexNode)node).getRHS(), reg );
            t.op = new Operator_type ();
            // Query node operators are 0=none, 1=and, 2=or, 3=andnot, 4=prox
            switch ( ((ComplexNode)node).getOp() )
            {
                case 1:
                    t.op.which = Operator_type.and_CID;
                    t.op.o = new AsnNull();
                    break;
                case 2:
                    t.op.which = Operator_type.or_CID;
                    t.op.o = new AsnNull();
                    break;
                case 3:
                    t.op.which = Operator_type.and_not_CID;
                    t.op.o = new AsnNull();
                    break;
                default:
                    System.err.println("Prox not yet handled");
                    break;
            }
        }
        else if ( node instanceof AttrPlusTermNode )
        {
            retval = new RPNStructure_type();
            retval.which = RPNStructure_type.op_CID;
            Operand_type t = new Operand_type();
            retval.o = t;

            t.which = Operand_type.attrterm_CID;
            AttributesPlusTerm_type apt = new AttributesPlusTerm_type();
            t.o = apt;

            apt.attributes = new Vector();

            Enumeration e = ((AttrPlusTermNode)node).getAttrEnum();

            for( ; e.hasMoreElements(); )
            {
                AttrTriple at = (AttrTriple) e.nextElement();
                AttributeElement_type ae = new AttributeElement_type();

                String element_attset = at.getAttrSet();

                // Use OID registry to lookup attr set oid if one is present
                if ( null != element_attset )
                {
                    ae.attributeSet = reg.oidByName(element_attset);
                }

                ae.attributeType = BigInteger.valueOf(at.getAttrType().intValue());;


                // Need to decide if the attribute value is a number or a complex
                Object attrval = at.getAttrVal();
                ae.attributeValue = new attributeValue_inline4_type();

                if ( attrval instanceof Integer )
                {
                    ae.attributeValue.which = attributeValue_inline4_type.numeric_CID ;
                    ae.attributeValue.o = BigInteger.valueOf(((Integer)attrval).intValue());
                }
                else if ( attrval instanceof String )
                {
                    String val = attrval.toString();

                    ae.attributeValue.which = attributeValue_inline4_type.complex_CID;

                    complex_inline5_type cit = new complex_inline5_type();
                    cit.list = new Vector();

                    ae.attributeValue.o = cit;

                    StringOrNumeric_type son = new StringOrNumeric_type();
                    son.which = StringOrNumeric_type.string_CID;
                    son.o = val;

                    cit.list.add(son);
                }
                else
                {
                    System.err.println("Unhandled type for attribute value in RPNHelper");
                    System.exit(1);
                }

                apt.attributes.add(ae);
            }

            // Add new AttributeElement members for each attribute
            apt.term = new Term_type();
            apt.term.which = Term_type.general_CID;
            apt.term.o = ((AttrPlusTermNode)node).getTerm();
        }

        // System.err.println(" visitNode returning "+retval);
        return retval;
    }

}
