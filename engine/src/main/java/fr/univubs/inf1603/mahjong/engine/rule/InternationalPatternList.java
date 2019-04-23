package fr.univubs.inf1603.mahjong.engine.rule;

import fr.univubs.inf1603.mahjong.engine.game.GameTile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.*;

import org.omg.CORBA.Current;

import java.util.ArrayList;

/**
 * Implementation of the {@link AbstractPatternList} for the international ruleset.
 * This class behaves like a singleton
 * @author Tristan Furno & Pierre Guriel & Anton Cosnefroy
 */
public class InternationalPatternList implements AbstractPatternList {

    public final static InternationalPatternList DEFAULT = new InternationalPatternList(InternationalPatterns.values());

    private IdentifiablePattern patterns[];

    public InternationalPatternList(IdentifiablePattern ... patterns) {
        this.patterns = patterns;
    }

    @Override
    public IdentifiablePattern[] getPatterns() {
        return this.patterns;
    }

    @Override
    public IdentifiablePattern getPattern(String name) {
        return InternationalPatterns.valueOf(name);
    }

    /**
     * The enumeration of all the possible patterns in the international ruleset.
     */
    private enum InternationalPatterns implements IdentifiablePattern {
        /**
         * 4 pungs des 4 vents
         */
        BIG_FOUR_WINDS {
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
                int nbCombination = 0;
                
                for (Combination temporaryCombination: allCombinations){
                    
                    if(temporaryCombination.isPung() || temporaryCombination.isKong()){
                        GameTile firstTile = temporaryCombination.getTiles()[0];
                        if(firstTile.getTile().toNormalizedName().equals("We")){
                            isEast = true;
                            pungFound.addAll(Arrays.asList(temporaryCombination.getTiles()));
                        }
                        else if(firstTile.getTile().toNormalizedName().equals("Ww")){
                            isWest = true;
                            pungFound.addAll(Arrays.asList(temporaryCombination.getTiles()));
                        }
                        else if(firstTile.getTile().toNormalizedName().equals("Ws")){
                            isSouth = true;
                            pungFound.addAll(Arrays.asList(temporaryCombination.getTiles()));
                        }
                        else if(firstTile.getTile().toNormalizedName().equals("Wn")){
                            isNorth = true;
                            pungFound.addAll(Arrays.asList(temporaryCombination.getTiles()));
                        }
                        nbCombination ++;
                    }
                    else if(nbCombination == 4 && temporaryCombination.isPair()){
                        pungFound.addAll(Arrays.asList(temporaryCombination.getTiles()));
                        nbCombination ++;
                    }
                }
                if(isEast && isWest && isSouth && isNorth && nbCombination == 5){
                    IdentifiedPattern pattern = new IdentifiedPattern(this, pungFound);
                    toReturn.add(pattern);
                }
                return toReturn;
            }
        },

        /**
         * 3 pungs des 3 dragons
         */
        BIG_THREE_DRAGONS {
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
                    
                    if(temporaryCombination.isPung() || temporaryCombination.isKong() || temporaryCombination.isChow()){
                        GameTile firstTile = temporaryCombination.getTiles()[0];
                        if(firstTile.getTile().toNormalizedName().equals("Dr") ){
                            isRed = true;
                        }
                        else if(firstTile.getTile().toNormalizedName().equals("Dw")){
                            isWhite = true;
                        }
                        else if(firstTile.getTile().toNormalizedName().equals("Dg")){
                            isGreen = true;
                        }
                        pungFound.addAll(Arrays.asList(temporaryCombination.getTiles()));
                        nbCombination ++;
                    } else if (nbCombination == 4 && temporaryCombination.isPair()){
                        pungFound.addAll(Arrays.asList(temporaryCombination.getTiles()));
                        nbCombination ++;
                    }
                }
                if(isRed && isWhite && isGreen && nbCombination == 5){
                    IdentifiedPattern pattern = new IdentifiedPattern(this, pungFound);
                    toReturn.add(pattern);
                }
                return toReturn;
            }
        },
        /**
         * 4 kongs
         */
        FOUR_KONGS {
            @Override
            public int getValue() {
                return 88;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                ArrayList<IdentifiedPattern> result = new ArrayList<>();
                Collection<Combination> allCombinations = set.getAllCombinations();

                int nbOfCombination = 0;
                Collection<GameTile> kongsFound = new ArrayList<>();

                for (Combination currentCombi: allCombinations) {
                    if (currentCombi.isKong()){
                        nbOfCombination++;
                        kongsFound.addAll(Arrays.asList(currentCombi.getTiles()));
                    }
                    else if(nbOfCombination == 4 && currentCombi.isPair()){
                        nbOfCombination ++;
                        kongsFound.addAll(Arrays.asList(currentCombi.getTiles()));
                    }
                }

                if (nbOfCombination == 5){
                    IdentifiedPattern pattern = new IdentifiedPattern(this, kongsFound);
                    result.add(pattern);
                }

                return result;
            }
        },
        /**
         * 7 Pairs consécutives dans une famille
         */
        SEVEN_SHIFTED_PAIRS {
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
                            if(!currentCombi.getTiles()[0].getTile().getPrevious().equals(lastCombi.getTiles()[0].getTile())){
                                isSameFamily = false;
                                break;
                            }
                        }
                        nbofPair ++;
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
                Collection<IdentifiedPattern> toReturn = new ArrayList<>();
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
        /**
         * 3 pungs de vente et paire du 4ème
         */
        LITTLE_FOUR_WINDS {
            @Override
            public int getValue() {
                return 64;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<Combination> allCombinations = set.getAllCombinations();
                Collection<IdentifiedPattern> toReturn = new ArrayList<>();
                Collection<GameTile> pungFound = new ArrayList<>();
                int windFound = 0;
                
                for (Combination temporaryCombination: allCombinations){
                    
                    if(temporaryCombination.isPung() || temporaryCombination.isKong()){
                        GameTile firstTile = temporaryCombination.getTiles()[0];
                        if(firstTile.getTile().toNormalizedName().equals("We")){
                            windFound++;
                            pungFound.addAll(Arrays.asList(temporaryCombination.getTiles()));
                        }
                        else if(firstTile.getTile().toNormalizedName().equals("Ww")){
                            windFound++;
                            pungFound.addAll(Arrays.asList(temporaryCombination.getTiles()));
                        }
                        else if(firstTile.getTile().toNormalizedName().equals("Ws")){
                            windFound++;
                            pungFound.addAll(Arrays.asList(temporaryCombination.getTiles()));
                        }
                        else if(firstTile.getTile().toNormalizedName().equals("Wn")){
                            windFound++;
                            pungFound.addAll(Arrays.asList(temporaryCombination.getTiles()));
                        }
                    }
                    else if(windFound == 3 && temporaryCombination.isPair()){
                        GameTile firstTile = temporaryCombination.getTiles()[0];
                        pattern = Pattern.compile("W(e|w|s|n)");
                        matcher = pattern.matcher(firstTile.toNormalizedName());
                        pungFound.addAll(Arrays.asList(temporaryCombination.getTiles()));
                        windFound++;
                    }
                }
                if(windFound == 4){
                    IdentifiedPattern pattern = new IdentifiedPattern(this, pungFound);
                    toReturn.add(pattern);
                }
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
        /**
         * Uniquement des 1 et des 9
         */
        ALL_TERMINALS {
            @Override
            public int getValue() {
                return 64;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                ArrayList<IdentifiedPattern> result = new ArrayList<>();
                Collection<Combination> allCombinations = set.getAllCombinations();
                int nbOfCombination = 0;
                Collection<GameTile> tilesFound = new ArrayList<>();
                boolean isAllHog = true;

                for (Combination currentCombi: allCombinations) {
                    if (currentCombi.isPung() || currentCombi.isKong()){
                        GameTile currentTile = currentCombi.getTiles()[0];
                            if(!currentTile.isMajor()){
                                isAllMajor = false;
                                break;
                            }
                        nbOfCombination++;
                        tilesFound.addAll(Arrays.asList(currentCombi.getTiles()));
                        lastCombi = currentCombi;
                    } else if(nbOfCombination == 4 && currentCombi.isPair()){
                        GameTile currentTile = currentCombi.getTiles()[0];
                            if(!currentTile.isMajor()){
                                isAllMajor = false;
                                break;
                            }
                        tilesFound.addAll(Arrays.asList(currentCombi.getTiles()));
                        nbOfCombination++;
                    }
                }

                if (nbOfCombination == 5 && isAllMajor){
                    IdentifiedPattern pattern = new IdentifiedPattern(this, tilesFound);
                    result.add(pattern);
                }

                return result;
            }
        },

        /**
         * 4 pongs cachés
         */
        FOUR_CONCEALED_PUNGS {
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
        /**
         * 4 chows identiques dane une famille
         */
        QUADRUPLE_CHOW {
            @Override
            public int getValue() {
                return 48;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                ArrayList<IdentifiedPattern> result = new ArrayList<>();
                Collection<Combination> allCombinations = set.getAllCombinations();
                int nbOfCombination = 0;
                boolean isSame = true;
                Collection<GameTile> chowFound = new ArrayList<>();
                Combination lastCombi = null;

                for (Combination currentCombi: allCombinations) {
                    if (currentCombi.isChow()){
                        if(nbOfCombination > 0){
                            if(!currentCombi.equals(lastCombi)){
                                isSame = false;
                                break;
                            }
                        }
                        nbOfCombination++;
                        chowFound.addAll(Arrays.asList(currentCombi.getTiles()));
                        lastCombi = currentCombi;
                    } else if(nbOfCombination == 4 && currentCombi.isPair()){
                        chowFound.addAll(Arrays.asList(currentCombi.getTiles()));
                        nbOfCombination ++;
                    }
                }

                if (nbOfCombination == 5 && isSame){
                    IdentifiedPattern pattern = new IdentifiedPattern(this, chowFound);
                    result.add(pattern);
                }

                return result;
            }
        },
        /**
         * 4 Pungs consécutifs dans une famille
         */
        FOUR_PURE_SHIFTED_PUNGS {
            @Override
            public int getValue() {
                return 48;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                ArrayList<IdentifiedPattern> result = new ArrayList<>();
                Collection<Combination> allCombinations = set.getAllCombinations();
                int nbOfCombination = 0;
                boolean isNextTile = true;
                Collection<GameTile> pungFound = new ArrayList<>();
                Combination lastCombi = null;

                for (Combination currentCombi: allCombinations) {
                    if (currentCombi.isPung()){
                        if(nbOfCombination > 0){
                            if(!currentCombi.getTiles()[0].getTile().equals(lastCombi.getTiles()[0].getTile().getNext())){
                                isNextTile = false;
                                break;
                            }
                        }
                        nbOfCombination++;
                        pungFound.addAll(Arrays.asList(currentCombi.getTiles()));
                        lastCombi = currentCombi;
                    } else if (nbOfCombination == 4 && currentCombi.isPair()){
                        pungFound.addAll(Arrays.asList(currentCombi.getTiles()));
                        nbOfCombination++;
                    }
                }

                if (nbOfCombination == 5 && isNextTile){
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
        /**
         * Uniquement des tuiles de 2 à 8
         */
        TITLE_HOG {
            @Override
            public int getValue() {
                return 2;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                ArrayList<IdentifiedPattern> result = new ArrayList<>();
                Collection<Combination> allCombinations = set.getAllCombinations();
                int nbOfCombination = 0;
                Collection<GameTile> tilesFound = new ArrayList<>();
                boolean isAllHog = true;

                for (Combination currentCombi: allCombinations) {
                    if (currentCombi.isChow() || currentCombi.isPung() || currentCombi.isKong()){
                        GameTile[] currentTiles = currentCombi.getTiles();
                        for(int i=0; i<currentTiles.length;i++){
                            pattern = Pattern.compile("(b|c|d)[2-8]");
                            matcher = pattern.matcher(currentTiles[i].toNormalizedName());
                            if(!matcher.matches()){
                                isAllHog = false;
                                break;
                            }
                        }
                        if(!isAllHog)break;
                        nbOfCombination++;
                        tilesFound.addAll(Arrays.asList(currentCombi.getTiles()));
                        lastCombi = currentCombi;
                    } else if(nbOfCombination == 4 && currentCombi.isPair()){
                        GameTile[] currentTiles = currentCombi.getTiles();
                        for(int i=0; i<currentTiles.length;i++){
                            pattern = Pattern.compile("(b|c|d)[2-8]");
                            matcher = pattern.matcher(currentTiles[i].toNormalizedName());
                            if(!matcher.matches()){
                                isAllHog = false;
                                break;
                            }
                        }
                        if(!isAllHog)break;
                        tilesFound.addAll(Arrays.asList(currentCombi.getTiles()));
                        nbOfCombination++;
                    }
                }

                if (nbOfCombination == 5 && isAllHog){
                    IdentifiedPattern pattern = new IdentifiedPattern(this, tilesFound);
                    result.add(pattern);
                }

                return result;
            }
        },
        /**
         * Deux chows identiques
         */
        PURE_DOUBLE_CHOW {
            @Override
            public int getValue() {
                return 1;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                ArrayList<IdentifiedPattern> result = new ArrayList<>();
                Collection<Combination> allCombinations = set.getAllCombinations();
                int nbOfCombination = 0;
                boolean isSame = true;
                Collection<GameTile> chowFound = new ArrayList<>();
                Combination lastCombi = null;

                for (Combination currentCombi: allCombinations) {
                    if (currentCombi.isChow()){
                        if(nbOfCombination > 0){
                            if(!currentCombi.equals(lastCombi)){
                                isSame = false;
                                break;
                            }
                        }
                        nbOfCombination++;
                        chowFound.addAll(Arrays.asList(currentCombi.getTiles()));
                        lastCombi = currentCombi;
                    } 
                }

                if (nbOfCombination == 2 && isSame){
                    IdentifiedPattern pattern = new IdentifiedPattern(this, chowFound);
                    result.add(pattern);
                }

                return result;
            
            }
        },
        /**
         * Deux chows identiques dans deux familles différentes
         */
        MIXED_DOUBLE_CHOW {
            @Override
            public int getValue() {
                return 1;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                ArrayList<IdentifiedPattern> result = new ArrayList<>();
                Collection<Combination> allCombinations = set.getAllCombinations();
                int nbOfCombination = 0;
                boolean isDifferent = true;
                boolean isSameNumber = false;
                Collection<GameTile> chowFound = new ArrayList<>();
                Combination lastCombi = null;

                for (Combination currentCombi: allCombinations) {
                    if (currentCombi.isChow()){
                        if(nbOfCombination > 0){
                            if(((CommonTile)currentCombi.getTiles()[0].getTile()).getFamily().equals(((CommonTile)lastCombi.getTiles()[0].getTile()).getFamily())){
                                isDifferent = false;
                                break;
                            }
                            if(((CommonTile)currentCombi.getTiles()[0].getTile()).getNumber().equals(((CommonTile)lastCombi.getTiles()[0].getTile()).getNumber())){
                                isSameNumber = true;
                            }
                        }
                        nbOfCombination++;
                        chowFound.addAll(Arrays.asList(currentCombi.getTiles()));
                        lastCombi = currentCombi;
                    } 
                }

                if (nbOfCombination == 2 && isSameNumber && isDifferent){
                    IdentifiedPattern pattern = new IdentifiedPattern(this, chowFound);
                    result.add(pattern);
                }

                return result;
            
            }
        },
        /**
         * Deux chow consécutifs dans une famille (suite de 6 tuiles)
         */
        SHORT_STRAIGHT {
            @Override
            public int getValue() {
                return 1;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                ArrayList<IdentifiedPattern> result = new ArrayList<>();
                Collection<Combination> allCombinations = set.getAllCombinations();
                int nbOfCombination = 0;
                boolean isSame = true;
                Collection<GameTile> chowFound = new ArrayList<>();
                Combination lastCombi = null;

                for (Combination currentCombi: allCombinations) {
                    if (currentCombi.isChow()){
                        if(nbOfCombination > 0){
                            if(!currentCombi.getTiles()[0].getTile().getPrevious().equals(lastCombi.getTiles()[2].getTile())){
                                isSame = false;
                                break;
                            }
                        }
                        nbOfCombination++;
                        chowFound.addAll(Arrays.asList(currentCombi.getTiles()));
                        lastCombi = currentCombi;
                    } 
                }

                if (nbOfCombination == 2 && isSame){
                    IdentifiedPattern pattern = new IdentifiedPattern(this, chowFound);
                    result.add(pattern);
                }

                return result;
            }
        },
        /**
         * 1-2-3 et 7-8-9 dans une famille
         */
        TWO_TERMINAL_CHOWS {
            @Override
            public int getValue() {
                return 1;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                ArrayList<IdentifiedPattern> result = new ArrayList<>();
                Collection<Combination> allCombinations = set.getAllCombinations();
                int nbOfCombination = 0;
                
                Collection<GameTile> chowFound = new ArrayList<>();
                Combination lastCombi = null;

                for (Combination currentCombi: allCombinations) {
                    if (currentCombi.isChow()){
                        if(nbOfCombination = 0){
                            if(((CommonTile)currentCombi.getTiles()[0].getTile()).isMajor()){
                                nbOfCombination++;
                                lastCombi = currentCombi;
                                chowFound.addAll(Arrays.asList(currentCombi.getTiles()));
                            }
                        } else if(nbOfCombination = 1){
                            if(((CommonTile)currentCombi.getTiles()[2].getTile()).isMajor()
                            && ((CommonTile)currentCombi.getTiles()[0].getTile()).getFamily().equals(
                                ((CommonTile)lastCombi.getTiles()[0].getTile()).getFamily())){
                                nbOfCombination ++;
                                chowFound.addAll(Arrays.asList(currentCombi.getTiles()));
                                
                            }
                        } else { break;}    
                    } 
                }

                if (nbOfCombination == 2){
                    IdentifiedPattern pattern = new IdentifiedPattern(this, chowFound);
                    result.add(pattern);
                }

                return result;
            }
        },
        /**
         * 1 pung de 1, de 9 ou de Vent
         */
        PUNG_OF_TERMINALS_OR_HONORS {
            @Override
            public int getValue() {
                return 1;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                ArrayList<IdentifiedPattern> toReturn = new ArrayList<>();
                Collection<Combination> allCombinations = set.getAllCombinations();
                
                
                Collection<GameTile> pungFound = new ArrayList<>();
                
                boolean isFind = false;

                for (Combination currentCombi: allCombinations) {
                    GameTile tile = currentCombi.getTiles()[0];
                    if (currentCombi.isPung() && (
                        ((CommonTile)tile.getTile()).isMajor() ||
                       //tile.getTile().toNormalizedName().equals("Dr") ||
                       //tile.getTile().toNormalizedName().equals("Dg") ||
                       //tile.getTile().toNormalizedName().equals("Dw") ||
                       tile.getTile().toNormalizedName().equals("Wn") ||
                       tile.getTile().toNormalizedName().equals("Ww") ||
                       tile.getTile().toNormalizedName().equals("We") ||
                       tile.getTile().toNormalizedName().equals("Ws")
                    )){
                        isFind = true;
                        pungFound.addAll(Arrays.asList(currentCombi.getTiles()));
                        break;    
                    }
                    
                }
                if (isFind){
                    IdentifiedPattern pattern = new IdentifiedPattern(this, pungFound);
                    toReturn.add(pattern);
                }

                return toReturn;
            }
        },
        /**
        * 1 kong exposé
        */
        MELDED_KONG {
            @Override
            public int getValue() {
                return 1;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                ArrayList<IdentifiedPattern> result = new ArrayList<>();
                Collection<Combination> allCombinations = set.getAllCombinations();
                Collection<GameTile> kongFound = new ArrayList<>();
                boolean isFind = false;
                for (Combination currentCombi: allCombinations) {
                    if(currentCombi.isKong()){
                        isFind = true;
                        kongFound.addAll(Arrays.asList(currentCombi.getTiles()));
                        break;  
                    }
                }
                if(isFind){
                    IdentifiedPattern pattern = new IdentifiedPattern(this, kongFound);
                    toReturn.add(pattern);
                }
                
                return result;
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
        /**
         * Uniquement des tuiles des familles
         */
        NO_HONORS {
            @Override
            public int getValue() {
                return 1;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                ArrayList<IdentifiedPattern> result = new ArrayList<>();
                Collection<Combination> allCombinations = set.getAllCombinations();
                int nbOfCombination = 0;
                Collection<GameTile> tilesFound = new ArrayList<>();
                boolean isAllHog = true;

                for (Combination currentCombi: allCombinations) {
                    if (currentCombi.isChow() || currentCombi.isPung() || currentCombi.isKong()){
                        GameTile[] currentTiles = currentCombi.getTiles();
                        for(int i=0; i<currentTiles.length;i++){
                            pattern = Pattern.compile("(b|c|d)[1-9]");
                            matcher = pattern.matcher(currentTiles[i].toNormalizedName());
                            if(!matcher.matches()){
                                isAllHog = false;
                                break;
                            }
                        }
                        if(!isAllHog)break;
                        nbOfCombination++;
                        tilesFound.addAll(Arrays.asList(currentCombi.getTiles()));
                        lastCombi = currentCombi;
                    } else if(nbOfCombination == 4 && currentCombi.isPair()){
                        nbOfCombination++;
                    }
                }

                if (nbOfCombination == 5 && isAllHog){
                    IdentifiedPattern pattern = new IdentifiedPattern(this, tilesFound);
                    result.add(pattern);
                }

                return result;
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
        /**
         * Fleur ou saison
         */
        FLOWER_TILES {
            @Override
            public int getValue() {
                return 1;
            }

            @Override
            public Collection<IdentifiedPattern> identify(PlayerSet set) {
                Collection<IdentifiedPattern> toReturn = new ArrayList<>();
                Collection<SupremeHonor> honorCollection = set.getSupremeHonors();
                
                IdentifiedPattern pattern = new IdentifiedPattern(this, honorCollection);
                toReturn.add(pattern);
                return toReturn;
            }
        }
    }
}
