import * as yup from 'yup';

// バリデーション定義（一元化）
export function ValidationItems() {
  const validationItems = {
    email: yup.string().email(),
  };
  return validationItems;
}
