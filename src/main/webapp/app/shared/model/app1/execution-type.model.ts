import dayjs from 'dayjs';

export interface IExecutionType {
  id?: number;
  uuid?: string;
  name?: string;
  label?: string | null;
  description?: string | null;
  createdAt?: dayjs.Dayjs;
}

export const defaultValue: Readonly<IExecutionType> = {};
