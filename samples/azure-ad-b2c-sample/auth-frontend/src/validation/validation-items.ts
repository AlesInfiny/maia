import * as yup from 'yup';

// バリデーション定義（一元化）
export const validationItems = {
  email: yup.string().email(),
};
