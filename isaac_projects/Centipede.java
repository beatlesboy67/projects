public class Centipede {

  protected long num;

  public Centipede() {
    num = 0;
  }

  // We have a number, num, and need to parse it
  // so that we can get cent values
  // We'll use a base 4 style of arithmetic,
  // 00 - penny, 01 - nickel, 10 - dime, 11 - quarter
  protected int sum_num(long number) {
    long after_mod = number;
    int sum = 0;
    long mod = after_mod % 4L;
    if (after_mod == 0L) {
      return 1;
    }

    while (after_mod > 0L ) {
      sum += parse_four(mod);

      after_mod = after_mod >> 2L;
      mod = after_mod & 3L;

    }
    return sum;
  }

  protected int parse_four(long number) {
    Long longer = new Long(number);
    Integer int_version = longer.intValue();
    switch(int_version) {
      case 0: return 1;
      case 1: return 5;
      case 2: return 10;
      case 3: return 25;
    }
    return 0;
  }

  protected void increment() {
    num++;
  }

  protected void evaluate_to(int[] targets) {
    System.out.println("Targets: ");
    for (int i = 0; i < 4; i++) {
      System.out.print(targets[i] + ", ");
    }
    while (!can_reach_all(targets)) {
      increment();
      if (num % 10000L == 0L) {
        System.out.println(num);
      }
    }
  }

  public void set_num(long number) {
    num = number;
  }

  protected boolean can_reach_pennies(int target) {
    int pennies = count_pennies();
    return pennies >= target;
  }
  // Function to tell if we can reach a certain sum
  // with current coins
  protected boolean can_reach(int target) {

    if (can_reach_pennies(target)) {
      return true;
    }

    // Permute through all types of num_coins
    // We're converting from pseudo base 4 to base 2,
    // so keep that in mind;
    // We need to shift over upper spots times 2
    int current = 1;
    long long_target = (long)target;
    long eval = 0L;
    while (Long.highestOneBit(doubler(current)) <= Long.highestOneBit(num << 1)) {
      eval = sum_num(double_merge(doubler(current)));
      if (eval == long_target) {
        return true;
      }
      current++;
    }

    return false;
  }

  protected int count_trailing_zeros(long number) {
    long cpy = number;
    int num_zeros = 0;
    while ((cpy & 1) == 0) {
      cpy = (cpy >> 1);
      num_zeros++;
    }
    return num_zeros;
  }


  // We can't naively & together num and our Doubled
  // number, we may haver stray zeros which get counted as pennies;
  protected long double_merge(long doubled) {
    // Scrub some zeros from both num and doubled.
    long temp_num = num;
    long ret = 0L;
    long temp_doubled = doubled;
    long lowest_two = 3L;
    int num_pennies = 0;
    while (temp_doubled > 0) {
      // Shift over all the zeros;
      int zeros = count_trailing_zeros(temp_doubled);
      temp_doubled = temp_doubled >> zeros;
      temp_num = temp_num >> zeros;

      // Take the lowest two:
      // Make room:
      if (ret == 0L && (temp_num & lowest_two) == 0L) {
        num_pennies+=1;
      }
      ret = ret << 2;
      ret |= temp_num & lowest_two;

      // Shift both temp_num and temp_doubled by two now:
      temp_num = temp_num >> 2;
      temp_doubled = temp_doubled >> 2;
    }

    if (num_pennies > 0) {
      ret = ret << num_pennies*2;
    }

    return ret;
  }

  protected boolean can_reach_all(int[] targets) {
    boolean ret = true;
    for (int i = 0; i < targets.length; i++) {
      ret = can_reach(targets[i]);
      if (!ret) {
        if (num % 100000L == 0L || num == 4169728L) {
          System.out.println("Cannot reach: " + targets[i]);
          while (i > 0) {
            i--;
            System.out.println("Can reach: " + targets[i]);
          }
          display_cents();

        }
        return false;
      }
    }

    return ret;
  }

  // 10

  protected long doubler(int number) {
    long ret = 0L;
    int count_bits = 0;
    int temp = number;
    while (temp >= 1) {

      count_bits++;
      if (temp == 1) {
        break;
      }
      temp = temp / 2;
    }
    int pos = 1 << count_bits;

    while (pos > 0) {
      int anded = (number & pos);
      ret |= (long)anded;
      ret = ret << 1;
      ret |= (long)anded;
      pos = pos >> 1;
    }

    return(ret);
  }

  protected int num_coins(Long number) {
    int num_coins = 0;
    Long temp = number;
    while (temp > 0L) {
      temp = temp / 4L;
      num_coins++;
    }
    return num_coins;
  }

  public void display_cents() {
    int num_q = 0;
    int num_n = 0;
    int num_p = 0;
    int num_d = 0;

    Long temp = num;
    Long mod = temp % 4L;
    while (temp > 0L) {
      switch(parse_four(mod)) {
        case 1: num_p++;
              break;
        case 5: num_n++;
              break;
        case 10: num_d++;
              break;
        case 25: num_q++;
              break;
      }
      temp = temp / 4L;
      mod = temp % 4L;
    }

    System.out.println("| There are " + num_coins(num) + " needed to reach target prices");
    System.out.println("| Quarters: " + num_q);
    System.out.println("| Dimes: " + num_d);
    System.out.println("| Nickels: " + num_n);
    System.out.println("| Pennies: " + num_p);
  }

  public int count_pennies() {
    int num_q = 0;
    int num_n = 0;
    int num_p = 0;
    int num_d = 0;

    Long temp = num;
    Long mod = temp % 4L;
    while (temp > 0L) {
      switch(parse_four(mod)) {
        case 1: num_p++;
              break;
        case 5: num_n++;
              break;
        case 10: num_d++;
              break;
        case 25: num_q++;
              break;
      }
      temp = temp / 4L;
      mod = temp % 4L;
    }
    return num_p;
}


  public void go_to(double[] cent_targets) {
    int[] int_target = new int[cent_targets.length];
    for (int i = 0; i < int_target.length; i++) {
      double t_num = cent_targets[i];
      t_num = 100d*t_num;
      int int_num = (int)t_num;
      int_target[i] = int_num % 100;
      if (int_target[i] == 0) {
        int_target[i] = 100;
      }
    }
    System.out.println("Last: " + int_target[int_target.length - 1]);
    evaluate_to(int_target);
    display_cents();
  }




  public static void main(String[] args) {
    Centipede cents = new Centipede();
    // 4.21 + 2.66
    //

    int end_range = 99;
    double[] cent_targets = new double[end_range];
    for (int i = 1; i < end_range + 1; i++) {
      cent_targets[i-1] = ((double)i / 100d);
    }
    // Disregards just pennies as an option by the way.
    cents.set_num(1042688L);
    cents.display_cents();
    //System.out.println(cents.can_reach(100));
    cents.go_to(cent_targets);
    //System.out.println("WINNING NUMBER: " + cents.num);
    //System.out.println(cents.double_merge(192L));

    return;
  }
}
