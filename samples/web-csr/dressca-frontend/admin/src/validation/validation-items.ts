import * as yup from 'yup';
import { toTypedSchema } from '@vee-validate/yup';
import type { TypedSchema } from 'vee-validate';

// バリデーション定義（一元化）
export const validationItems = {
  email: yup.string().email(),
};

/**
 * カタログアイテムのバリデーションを定義する型付きのスキーマです。
 */
export const catalogItemSchema: TypedSchema = toTypedSchema(
  yup.object({
    name: yup
      .string()
      .required('アイテム名は必須です。')
      .max(256, '256文字以下で入力してください。'),
    description: yup
      .string()
      .required('説明は必須です。')
      .max(1024, '1024文字以下で入力してください。'),
    price: yup
      .number()
      .required('単価は必須です。')
      .typeError('数値で入力してください。')
      .integer('整数で入力してください。')
      .positive('正の数で入力してください。'),
    productCode: yup
      .string()
      .required('商品コードは必須です。')
      .max(128, '128文字以下で入力してください。')
      .matches(/^[0-9a-zA-Z]+$/, '半角英数字で入力してください。'),
  }),
);
