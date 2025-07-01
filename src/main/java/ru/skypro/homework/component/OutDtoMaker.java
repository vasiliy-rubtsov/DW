package ru.skypro.homework.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.AdModel;
import ru.skypro.homework.model.CommentModel;
import ru.skypro.homework.model.UserModel;

import java.util.List;

@Component
public class OutDtoMaker {
    @Value("${ads.image-uri}")
    private String imageUri;

    private String getFullImageUrl(String imageFileName) {
        if (imageFileName == null) {
            return null;
        }
        return imageUri + "/" + imageFileName;
    }

    public Ad makeAd(AdModel adModel) {
        Ad result = new Ad();

        result.setImage(getFullImageUrl(adModel.getImage()));
        result.setAuthor(adModel.getAuthor().getId());
        result.setTitle(adModel.getTitle());
        result.setPrice(adModel.getPrice());
        result.setPk(adModel.getId());

        return result;
    }

    public Ads makeAds(List<AdModel> ads) {
        Ads result = new Ads();
        for (AdModel adModel : ads) {
            result.addAd(makeAd(adModel));
        }

        return result;
    }

    public Comment makeComment(CommentModel commentModel) {
        Comment result = new Comment();
        result.setPk(commentModel.getId());
        result.setAuthor(commentModel.getAuthor().getId());
        result.setText(commentModel.getText());
        result.setAuthorFirstName(commentModel.getAuthor().getFirstName());
        result.setAuthorImage(getFullImageUrl(commentModel.getAuthor().getImage()));
        result.setCreatedAt(commentModel.getCreatedAt().getTime());

        return result;
    }


    public Comments makeComments(List<CommentModel> commentModels) {
        Comments comments = new Comments();

        for (CommentModel commentModel : commentModels) {
            comments.addComment(makeComment(commentModel));
        }

        return comments;
    }

    public User makeUser(UserModel userModel) {
        User user = new User();
        user.setImage(getFullImageUrl(userModel.getImage()));
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setEmail(userModel.getEmail());
        user.setPhone(userModel.getPhone());
        user.setRole(userModel.getRole());
        user.setId(userModel.getId());

        return user;
    }

    public ExtendedAd  makeExtendedAd(AdModel adModel) {
        ExtendedAd result = new ExtendedAd();

        result.setImage(getFullImageUrl(adModel.getImage()));
        result.setTitle(adModel.getTitle());
        result.setPrice(adModel.getPrice());
        result.setDescription(adModel.getDescription());
        result.setPk(adModel.getId());

        UserModel author = adModel.getAuthor();
        result.setEmail(author.getEmail());
        result.setPhone(author.getPhone());
        result.setAuthorFirstName(author.getFirstName());
        result.setAuthorLastName(author.getLastName());

        return result;

    }
}
