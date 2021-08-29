public class rotate_matrix {
  public int row_length;
  public int[][] matrix;

  public rotate_matrix(int size) {
    row_length = size;
    matrix = new int[size][size];
    for (int i = 0; i < row_length * row_length; i++) {
      matrix[i / row_length][i % row_length] = i;
    }
  }

  public void rotate(int degrees) {
    for (int rotation = 0; rotation < degrees / 90; rotation++) {
      transpose();
      reflect_y();
    }
  }

  protected void transpose() {
    for (int x_val = 0; x_val < row_length; x_val++) {
      for (int y_val = 0; y_val < x_val; y_val++) {
        swap_coord(x_val, y_val, y_val, x_val);
      }
    }
  }

  protected void reflect_y() {
    for (int row_num = 0; row_num < row_length; row_num++) {
      reverse_row(row_num);
    }
  }

  protected void reverse_row(int row_num) {
    for (int index = 0; index < row_length /2; index++) {
      swap_coord(row_num, index, row_num, row_length - index);
    }
  }

  public rotate_matrix oop_rotate(int degrees) {
    rotate_matrix rot = new rotate_matrix(row_length);
    for (int row_num = 0; row_num < row_length; row_num++) {
      rot.matrix[row_num] = col_to_row(row_num);
    }

    return rot;
  }

  public int[] col_to_row(int col_num) {
    int[] ret_row = new int[row_length];
    int pos = 0;
    while (pos < row_length) {
      ret_row[pos] = matrix[row_length - pos - 1][col_num];
      pos++;
    }

    return ret_row;
  }

  public String toString() {
    String ret = "\n | ";
    for (int i = 0; i < row_length * row_length; i++) {
      ret += matrix[i / row_length][i % row_length];
      ret += " ";
      if (i%row_length == row_length - 1 & i < (row_length * row_length) - 1) {
        ret += "\n | ";
      }
    }
    return ret;
  }

  protected void swap(int pos_one, int pos_two) {
    int temp = matrix[pos_one / row_length][pos_one % row_length];
    matrix[pos_one / row_length][pos_one % row_length] = matrix[pos_two / row_length][pos_two % row_length];
    matrix[pos_two / row_length][pos_two % row_length] = temp;
  }

  protected  void swap_coord(int x1, int x2, int y1, int y2) {

    int temp = matrix[x1][y1];
    matrix[x1][y1] = matrix[x2][y2];
    matrix[x2][y2] = temp;
  }

  public static void main(String[] args) {
    rotate_matrix mat = new rotate_matrix(10);
    System.out.println(mat.toString());
    mat.rotate(180);
    System.out.println(mat.toString());
  }
}



/*
 * 1 2 3 4
 * 5 6 7 8
 * 9 10 11 12
 * 13 14 15 16
 *
 * 1 -4 - 16 - 13 (1 13) (13 16)(16 4) (12, 3, 12, 3)
 * 2 - 8 - 15 - 9 (6, 7, 6, 7) (6, 7, 10, 9)
 * 3 - 12 - 14 - 5
 * 6 - 7 - 11- 10
 */
