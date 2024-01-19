import dayjs from 'dayjs';
import { SPentType } from 'app/shared/model/enumerations/s-pent-type.model';
import { StatusCharges } from 'app/shared/model/enumerations/status-charges.model';

export interface ISocialCharges {
  id?: number;
  spentDate?: dayjs.Dayjs | null;
  spentType?: keyof typeof SPentType | null;
  statusCharges?: keyof typeof StatusCharges | null;
  amount?: number;
  purchaseManager?: string | null;
  commentText?: string;
}

export const defaultValue: Readonly<ISocialCharges> = {};
