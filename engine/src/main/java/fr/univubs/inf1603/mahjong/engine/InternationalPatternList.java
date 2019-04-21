package fr.univubs.inf1603.mahjong.engine;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Implementation of the {@link AbstractPatternList} for the international ruleset.
 * This class behaves like a singleton
 * @author Tristan Furno & Pierre Guriel
 */
public class InternationalPatternList implements AbstractPatternList {

    private static InternationalPatternList INSTANCE = new InternationalPatternList();
//    private int maxPoint = 88;

    private InternationalPatternList() {}

    public static synchronized InternationalPatternList getInstance() {
        return INSTANCE;
    }
/*
    public void setMaxPoint(int maxPoint) {
        this.maxPoint = maxPoint;
    }
*/

    @Override
    public IdentifiablePattern[] getPatterns() {
        return InternationalPatterns.values();
    }

    @Override
    public IdentifiablePattern getPattern(String name) {
        return InternationalPatterns.valueOf(name);
    }

    /**
     * The enumeration of all the possible patterns in the international ruleset.
     */
    private enum InternationalPatterns implements IdentifiablePattern {

        BIG_FOUR_WINDS { //4 pungs of 4 winds
            @Override
            public int getValue() {
                return 88;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<Combination> allCombinations = set.getAllCombinations();
                Collection<IdentifiedPattern> toReturn = new ArrayList<>();
                Collection<GameTile> pungFound = new ArrayList<>();
                boolean isWest = false;
                boolean isEast = false;
                boolean isNorth = false;
                boolean isSouth = false;
                
                for (Combination temporaryCombination: allCombinations){
                    
                    if(temporaryCombination.isPung()){
                        GameTile firstTile = temporaryCombination.getTiles()[0];
                        if(firstTile.getTile().toNormalizedName() == "We"){
                            isEast = true;
                            pungFound.addAll(Arrays.asList(temporaryCombination.getTiles()));
                        }
                        else if(firstTile.getTile().toNormalizedName() == "Ww"){
                            isWest = true;
                            pungFound.addAll(Arrays.asList(temporaryCombination.getTiles()));
                        }
                        else if(firstTile.getTile().toNormalizedName() == "Ws"){
                            isSouth = true;
                            pungFound.addAll(Arrays.asList(temporaryCombination.getTiles()));
                        }
                        else if(firstTile.getTile().toNormalizedName() == "Wn" ){
                            isNorth = true;
                            pungFound.addAll(Arrays.asList(temporaryCombination.getTiles()));
                        }
                    }
                }
                if(isEast && isWest && isSouth && isNorth){
                    IdentifiedPattern pattern = new IdentifiedPattern(this, pungFound);
                    toReturn.add(pattern);
                }
                return toReturn;
            }
        },

        BIG_THREE_DRAGONS { //3 pungs of the 3 dragons
            @Override
            public int getValue() {
                return 88;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<Combination> allCombinations = set.getAllCombinations();
                Collection<IdentifiedPattern> toReturn = new ArrayList<>();
                Collection<GameTile> pungFound = new ArrayList<>();
                boolean isRed = false;
                boolean isWhite = false;
                boolean isGreen = false;
                for (Combination temporaryCombination: allCombinations){
                    
                    if(temporaryCombination.isPung()){
                        GameTile firstTile = temporaryCombination.getTiles()[0];
                        if(firstTile.getTile().toNormalizedName() == "Dr" ){
                            isRed = true;
                            
                        }
                        else if(firstTile.getTile().toNormalizedName() == "Dw" ){
                            isWhite = true;
                        }
                        else if(firstTile.getTile().toNormalizedName() == "Dg"){
                            isGreen = true;
                        }
                    }
                }
                if(isRed && isWhite && isGreen){
                    IdentifiedPattern pattern = new IdentifiedPattern(this, pungFound);
                    toReturn.add(pattern);
                }
                return toReturn;
            }
        },

        FOUR_KONGS { //The name is obvious
            @Override
            public int getValue() {
                return 88;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                ArrayList<IdentifiedPattern> result = new ArrayList<>();
                Collection<Combination> allCombinations = set.getAllCombinations();

                int nbOfKong = 0;
                Collection<GameTile> kongsFound = new ArrayList<>();

                for (Combination currentCombi: allCombinations) {
                    if (currentCombi.isKong()){
                        nbOfKong++;
                        kongsFound.addAll(Arrays.asList(currentCombi.getTiles()));
                    }
                }

                if (nbOfKong == 4){
                    IdentifiedPattern pattern = new IdentifiedPattern(this, kongsFound);
                    result.add(pattern);
                }

                return result;
            }
        },
        
        SEVEN_SHIFTED_PAIRS { //7 pair in one family
            @Override
            public int getValue() {
                return 88;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                ArrayList<IdentifiedPattern> result = new ArrayList<>();
                Collection<Combination> allCombinations = set.getAllCombinations();
                Collection<GameTile> pairsFound = new ArrayList<>();
                Combination lastCombi = null;
                boolean isSameFamily = true;
                int nbofPair = 0;
                
                for (Combination currentCombi: allCombinations) {
                    if(currentCombi.isPair()){
                        if(nbofPair > 0){
                            if(!currentCombi.getTiles()[0].getPrevious().equals(lastCombi.getTiles()[0])){
                                isSameFamily = false;
                                break;
                            }
                        }
                        nbofPair ++;
                        GameTile firstTile = currentCombi.getTiles()[0];
                        pairsFound.addAll(Arrays.asList(currentCombi.getTiles()));
                        lastCombi = currentCombi;
                       
                    } else {
                        break;
                    }
                    
                    
                    
                }
                if(nbofPair == 7 && isSameFamily) {
                    IdentifiedPattern pattern = new IdentifiedPattern(this, pairsFound);
                    result.add(pattern);
                }
                
                return result;
            }
        },


        ALL_GREEN {
            @Override
            public int getValue() {
                return 88;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        NINE_GATES {
            @Override
            public int getValue() {
                return 88;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        THIRTEEN_ORPHANS {
            @Override
            public int getValue() {
                return 88;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        LITTLE_FOUR_WINDS {
            @Override
            public int getValue() {
                return 64;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        LITTLE_THREE_DRAGONS {
            @Override
            public int getValue() {
                return 64;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        ALL_HONORS {
            @Override
            public int getValue() {
                return 64;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        PURE_TERMINAL_CHOWS {
            @Override
            public int getValue() {
                return 64;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        ALL_TERMINALS {
            @Override
            public int getValue() {
                return 64;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },


        FOUR_CONCEALED_PUNGS { //Add when pung is concealed
            @Override
            public int getValue() {
                return 64;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                ArrayList<IdentifiedPattern> result = new ArrayList<>();
                Collection<Combination> allCombinations = set.getAllCombinations();
                boolean isConcealed = true;

                int nbOfPungConcealed = 0;
                Collection<GameTile> pungFound = new ArrayList<>();

                for (Combination currentCombi: allCombinations) {
                    if (currentCombi.isPung()){
                        nbOfPungConcealed++;
                        pungFound.addAll(Arrays.asList(currentCombi.getTiles()));
                    }
                }

                if (nbOfPungConcealed == 4){
                    IdentifiedPattern pattern = new IdentifiedPattern(this, pungFound);
                    result.add(pattern);
                }

                return result;
            }
        },

        QUADRUPLE_CHOW { //4 same chow
            @Override
            public int getValue() {
                return 48;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                ArrayList<IdentifiedPattern> result = new ArrayList<>();
                Collection<Combination> allCombinations = set.getAllCombinations();
                int nbOfChow = 0;
                boolean isSame = true;
                Collection<GameTile> chowFound = new ArrayList<>();
                Combination lastCombi = null;

                for (Combination currentCombi: allCombinations) {
                    if (currentCombi.isChow()){
                        if(nbOfChow > 0){
                            if(!currentCombi.getTiles()[0].equals(lastCombi.getTiles()[0])){
                                isSame = false;
                                break;
                            }
                        }
                        nbOfChow++;
                        chowFound.addAll(Arrays.asList(currentCombi.getTiles()));
                        lastCombi = currentCombi;
                    }
                }

                if (nbOfChow == 4 && isSame){
                    IdentifiedPattern pattern = new IdentifiedPattern(this, chowFound);
                    result.add(pattern);
                }

                return result;
            }
        },

        FOUR_PURE_SHIFTED_PUNGS {
            @Override
            public int getValue() {
                return 48;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                ArrayList<IdentifiedPattern> result = new ArrayList<>();
                Collection<Combination> allCombinations = set.getAllCombinations();
                int nbOfPung = 0;
                boolean isNextTile = true;
                Collection<GameTile> pungFound = new ArrayList<>();
                Combination lastCombi = null;

                for (Combination currentCombi: allCombinations) {
                    if (currentCombi.isPung()){
                        if(nbOfPung > 0){
                            if(!currentCombi.getTiles()[0].equals(lastCombi.getTiles()[0].getNext())){
                                isNextTile = false;
                                break;
                            }
                        }
                        nbOfPung++;
                        pungFound.addAll(Arrays.asList(currentCombi.getTiles()));
                        lastCombi = currentCombi;
                    }
                }

                if (nbOfPung == 4 && isNextTile){
                    IdentifiedPattern pattern = new IdentifiedPattern(this, pungFound);
                    result.add(pattern);
                }

                return result;
            }
        },
        
        FOUR_PURE_SHIFTED_CHOWS {
            @Override
            public int getValue() {
                return 32;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        ALL_TERMINALS_AND_HONORS {
            @Override
            public int getValue() {
                return 32;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        THREE_KONGS {
            @Override
            public int getValue() {
                return 32;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },

        PURE_TRIPLE_CHOW {
            @Override
            public int getValue() {
                return 24;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        ALL_EVEN_PUNGS {
            @Override
            public int getValue() {
                return 24;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        PURE_SHIFTED_PUNGS {
            @Override
            public int getValue() {
                return 24;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        SEVEN_PAIRS {
            @Override
            public int getValue() {
                return 24;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        FULL_FLUSH {
            @Override
            public int getValue() {
                return 24;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        UPPER_TILES {
            @Override
            public int getValue() {
                return 24;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        MIDDLE_TILES {
            @Override
            public int getValue() {
                return 24;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        LOWER_TILES {
            @Override
            public int getValue() {
                return 24;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        GREATER_HONORS_AND_KNITTED_TILES {
            @Override
            public int getValue() {
                return 24;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        PURE_SHIFTED_CHOWS {
            @Override
            public int getValue() {
                return 16;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        PURE_STRAIGHT {
            @Override
            public int getValue() {
                return 16;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        THREE_SUITED_TERMINAL_CHOWS {
            @Override
            public int getValue() {
                return 16;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        TRIPLE_PUNG {
            @Override
            public int getValue() {
                return 16;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        THREE_CONCEALED_PUNGS {
            @Override
            public int getValue() {
                return 16;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        ALL_FIVES {
            @Override
            public int getValue() {
                return 16;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },

        BIG_THREE_WINDS {
            @Override
            public int getValue() {
                return 12;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        KNITTED_STRAIGHT {
            @Override
            public int getValue() {
                return 12;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        UPPER_FOUR {
            @Override
            public int getValue() {
                return 12;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        LOWER_FOUR {
            @Override
            public int getValue() {
                return 12;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        LESSER_HONORS_AND_KNITTED_TILES {
            @Override
            public int getValue() {
                return 12;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        MIXED_TRIPLE_CHOWS {
            @Override
            public int getValue() {
                return 8;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        MIXED_STRAIGHT {
            @Override
            public int getValue() {
                return 8;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        MIXED_SHIFTED_PUNGS {
            @Override
            public int getValue() {
                return 8;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        TWO_CONCEALED_KONGS {
            @Override
            public int getValue() {
                return 8;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        LAST_TILE_DRAW {
            @Override
            public int getValue() {
                return 8;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        LAST_TILE_CLAIM {
            @Override
            public int getValue() {
                return 8;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        OUT_WITH_REPLACEMENT_TILE {
            @Override
            public int getValue() {
                return 8;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        ROBBING_THE_KONG {
            @Override
            public int getValue() {
                return 8;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        REVERSIBLE_TILES {
            @Override
            public int getValue() {
                return 8;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        CHICKEN_HAND {
            @Override
            public int getValue() {
                return 8;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        TWO_DRAGON_PUNGS {
            @Override
            public int getValue() {
                return 6;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        MIXED_SHIFTED_CHOWS {
            @Override
            public int getValue() {
                return 6;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        ALL_PUNGS {
            @Override
            public int getValue() {
                return 6;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        HALF_FLUSH {
            @Override
            public int getValue() {
                return 6;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        ALL_TYPES {
            @Override
            public int getValue() {
                return 6;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        MELDED_HAND {
            @Override
            public int getValue() {
                return 6;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },

        TWO_MELDED_KONGS {
            @Override
            public int getValue() {
                return 4;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        OUTSIDE_HAND {
            @Override
            public int getValue() {
                return 4;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        FULLY_CONCEALED_HAND {
            @Override
            public int getValue() {
                return 4;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        LAST_TILE {
            @Override
            public int getValue() {
                return 4;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        DRAGON_PUNG {
            @Override
            public int getValue() {
                return 2;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        PREVALENT_WIND {
            @Override
            public int getValue() {
                return 2;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        SEAT_WIND {
            @Override
            public int getValue() {
                return 2;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        ALL_CHOWS {
            @Override
            public int getValue() {
                return 2;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        DOUBLE_PUNG {
            @Override
            public int getValue() {
                return 2;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        TWO_CONCEALED_PUNGS {
            @Override
            public int getValue() {
                return 2;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        CONCEALED_KONG {
            @Override
            public int getValue() {
                return 2;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        ALL_SIMPLES {
            @Override
            public int getValue() {
                return 2;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        CONCEALED_HAND {
            @Override
            public int getValue() {
                return 2;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        TITLE_HOG {
            @Override
            public int getValue() {
                return 2;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        PURE_DOUBLE_CHOW {
            @Override
            public int getValue() {
                return 1;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        MIXED_DOUBLE_CHOW {
            @Override
            public int getValue() {
                return 1;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        SHORT_STRAIGHT {
            @Override
            public int getValue() {
                return 1;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        TWO_TERMINAL_CHOWS {
            @Override
            public int getValue() {
                return 1;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        PUNG_OF_TERMINALS_OR_HONORS {
            @Override
            public int getValue() {
                return 1;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        MELDED_KONG {
            @Override
            public int getValue() {
                return 1;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        ONE_VOIDED_SUIT {
            @Override
            public int getValue() {
                return 1;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        NO_HONORS {
            @Override
            public int getValue() {
                return 1;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        EDGE_WAIT {
            @Override
            public int getValue() {
                return 1;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        CLOSED_WAIT {
            @Override
            public int getValue() {
                return 1;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        SINGLE_WAIT {
            @Override
            public int getValue() {
                return 1;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        SELF_DRAW {
            @Override
            public int getValue() {
                return 1;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        },
        FLOWER_TILES {
            @Override
            public int getValue() {
                return 1;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new Vector<>();
                return toReturn;
            }
        }
    }
}
