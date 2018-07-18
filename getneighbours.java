public int getNeighborAmount (Flower flower,PlayerColor color) {
    HashSet<Flower> myset=null;
    if(color==PlayerColor.Red){
      myset=redflowerset;
    }else{
      myset=blueflowerset;
    }
    int number=0;
    for(Flower f:myset){
      if(isNeighbours (f,flower)){
        number++;
      }
    }
    return number;
  }
  public boolean isNeighbours (Flower flower1,Flower flower2){
    Position pos1_1=flower1.getFirst();
    Position pos1_2=flower1.getSecond();
    Position pos1_3=flower1.getThird();
    ArrayList<Position> position=new ArrayList();
    position.add(pos1_1);
    position.add(pos1_2);
    position.add(pos1_3);
    Position pos2_1=flower2.getFirst();
    Position pos2_2=flower2.getSecond();
    Position pos2_3=flower2.getThird();
    Position []pos={pos2_1,pos2_2,pos2_3};
    int number=0;
    for(int i=0;i<3;i++){
      if(position.contains(pos[i])){
        number++;
      }
    }
    if(number==2){
      return true;
    }
    else{
      return false;
    }
  }