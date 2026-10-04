package com.example.smartpantrymanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class IngredientAdapter
        extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<Ingredient> ingredientList;
    private Context context;

    public IngredientAdapter(
            Context context,
            List<Ingredient> ingredientList) {

        this.context = context;
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);

        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull IngredientViewHolder holder,
            int position) {

        Ingredient ingredient = ingredientList.get(position);

        holder.textIngredientName.setText(
                ingredient.getName()
        );

        holder.textIngredientDetails.setText(
                context.getString(R.string.quantity_label) +
                        ingredient.getQuantity() +
                        " " +
                        ingredient.getUnit()
        );

        holder.textIngredientCategory.setText(
                context.getString(R.string.category_label) +
                        ingredient.getCategory()
        );

        holder.textIngredientMinStock.setText(
                context.getString(R.string.minimum_stock_label) +
                        ingredient.getMinStock() +
                        " " +
                        ingredient.getUnit()
        );

        holder.textIngredientExpiry.setText(
                context.getString(R.string.expiry_label) +
                        ingredient.getExpiryDate()
        );

        if (ingredient.getQuantity() <= ingredient.getMinStock()) {

            holder.textIngredientStockWarning.setVisibility(
                    View.VISIBLE
            );

            holder.textIngredientStockWarning.setText(
                    context.getString(R.string.low_stock)
            );

        } else {

            holder.textIngredientStockWarning.setVisibility(
                    View.GONE
            );
        }

        checkExpiry(
                ingredient.getExpiryDate(),
                holder.textIngredientExpiryWarning
        );

        holder.btnEditIngredient.setOnClickListener(v -> {

            Intent intent = new Intent(
                    context,
                    AddEditIngredientActivity.class
            );

            intent.putExtra(
                    "ingredient_id",
                    ingredient.getId()
            );

            intent.putExtra(
                    "ingredient_name",
                    ingredient.getName()
            );

            intent.putExtra(
                    "ingredient_quantity",
                    ingredient.getQuantity()
            );

            intent.putExtra(
                    "ingredient_unit",
                    ingredient.getUnit()
            );

            intent.putExtra(
                    "ingredient_expiry",
                    ingredient.getExpiryDate()
            );

            intent.putExtra(
                    "ingredient_category",
                    ingredient.getCategory()
            );

            intent.putExtra(
                    "ingredient_min_stock",
                    ingredient.getMinStock()
            );

            context.startActivity(intent);
        });

        holder.btnDeleteIngredient.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle(
                            context.getString(
                                    R.string.delete_ingredient
                            )
                    )
                    .setMessage(
                            context.getString(
                                    R.string.delete_confirmation
                            )
                    )
                    .setPositiveButton(
                            context.getString(R.string.delete),
                            (dialog, which) -> {

                                DatabaseHelper databaseHelper =
                                        new DatabaseHelper(context);

                                databaseHelper.deleteIngredient(
                                        ingredient.getId()
                                );

                                ingredientList.remove(position);

                                notifyItemRemoved(position);

                                notifyItemRangeChanged(
                                        position,
                                        ingredientList.size()
                                );
                            }
                    )
                    .setNegativeButton(
                            context.getString(R.string.cancel),
                            null
                    )
                    .show();
        });
    }

    private void checkExpiry(
            String expiryDateString,
            TextView warningTextView) {

        if (expiryDateString == null ||
                expiryDateString.trim().isEmpty()) {

            warningTextView.setVisibility(
                    View.GONE
            );

            return;
        }

        SimpleDateFormat dateFormat =
                new SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                );

        dateFormat.setLenient(false);

        try {

            Date expiryDate =
                    dateFormat.parse(
                            expiryDateString.trim()
                    );

            Calendar today =
                    Calendar.getInstance();

            Calendar expiry =
                    Calendar.getInstance();

            expiry.setTime(expiryDate);

            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            expiry.set(Calendar.HOUR_OF_DAY, 0);
            expiry.set(Calendar.MINUTE, 0);
            expiry.set(Calendar.SECOND, 0);
            expiry.set(Calendar.MILLISECOND, 0);

            if (expiry.before(today)) {

                warningTextView.setVisibility(
                        View.VISIBLE
                );

                warningTextView.setText(
                        context.getString(R.string.expired)
                );

            } else {

                Calendar sevenDaysFromNow =
                        Calendar.getInstance();

                sevenDaysFromNow.set(Calendar.HOUR_OF_DAY, 0);
                sevenDaysFromNow.set(Calendar.MINUTE, 0);
                sevenDaysFromNow.set(Calendar.SECOND, 0);
                sevenDaysFromNow.set(Calendar.MILLISECOND, 0);

                sevenDaysFromNow.add(
                        Calendar.DAY_OF_YEAR,
                        7
                );

                if (!expiry.after(sevenDaysFromNow)) {

                    warningTextView.setVisibility(
                            View.VISIBLE
                    );

                    warningTextView.setText(
                            context.getString(
                                    R.string.expiring_soon
                            )
                    );

                } else {

                    warningTextView.setVisibility(
                            View.GONE
                    );
                }
            }

        } catch (ParseException e) {

            warningTextView.setVisibility(
                    View.GONE
            );
        }
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public void updateList(List<Ingredient> newList) {

        ingredientList = newList;

        notifyDataSetChanged();
    }

    public static class IngredientViewHolder
            extends RecyclerView.ViewHolder {

        TextView textIngredientName;
        TextView textIngredientDetails;
        TextView textIngredientCategory;
        TextView textIngredientMinStock;
        TextView textIngredientStockWarning;
        TextView textIngredientExpiry;
        TextView textIngredientExpiryWarning;

        Button btnEditIngredient;
        Button btnDeleteIngredient;

        public IngredientViewHolder(
                @NonNull View itemView) {

            super(itemView);

            textIngredientName =
                    itemView.findViewById(
                            R.id.textIngredientName
                    );

            textIngredientDetails =
                    itemView.findViewById(
                            R.id.textIngredientDetails
                    );

            textIngredientCategory =
                    itemView.findViewById(
                            R.id.textIngredientCategory
                    );

            textIngredientMinStock =
                    itemView.findViewById(
                            R.id.textIngredientMinStock
                    );

            textIngredientStockWarning =
                    itemView.findViewById(
                            R.id.textIngredientStockWarning
                    );

            textIngredientExpiry =
                    itemView.findViewById(
                            R.id.textIngredientExpiry
                    );

            textIngredientExpiryWarning =
                    itemView.findViewById(
                            R.id.textIngredientExpiryWarning
                    );

            btnEditIngredient =
                    itemView.findViewById(
                            R.id.btnEditIngredient
                    );

            btnDeleteIngredient =
                    itemView.findViewById(
                            R.id.btnDeleteIngredient
                    );
        }
    }
}